package com.dsh.common.map.google;

import com.dsh.common.map.vo.AddressComponentsVo;
import com.dsh.common.map.vo.DistancematrixVo;
import com.dsh.common.map.vo.FindPlaceFromTextVo;
import com.dsh.common.map.vo.GeocodeVo;
import com.dsh.common.map.vo.ReverseGeocodeVo;
import com.google.maps.*;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Google Maps Utility
 * Provides geocoding, reverse geocoding, place search, distance matrix, and directions.
 *
 * @author saptariwijaya4@gmail.com
 */
@Component
public class GoogleMapUtil {

    @Value("${map.google.api-key}")
    private String key;

    /**
     * Forward geocoding: address -> coordinates
     *
     * @param address Address string
     * @return GeocodeVo with lat/lng, or null if not found
     */
    public GeocodeVo getGeocode(String address) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address)
                    .language("en")
                    .await();
            if (results.length > 0) {
                LatLng location = results[0].geometry.location;
                GeocodeVo vo = new GeocodeVo();
                vo.setLat(location.lat);
                vo.setLng(location.lng);
                return vo;
            }
            return null;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Reverse geocoding: coordinates -> address
     *
     * @param lat Latitude
     * @param lng Longitude
     * @return ReverseGeocodeVo with address and components, or null if not found
     */
    public ReverseGeocodeVo getReverseGeocode(double lat, double lng) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(lat, lng))
                    .language("id")
                    .await();
            if (results.length > 0) {
                // results[0] sering bertipe plus_code yang komponennya hanya kota/provinsi/negara;
                // pakai hasil non-plus_code pertama (street_address/premise/route/dst) agar lebih spesifik
                GeocodingResult result = results[0];
                for (GeocodingResult r : results) {
                    boolean plusCode = false;
                    if (r.types != null) {
                        for (AddressType type : r.types) {
                            if (type == AddressType.PLUS_CODE) {
                                plusCode = true;
                                break;
                            }
                        }
                    }
                    if (!plusCode) {
                        result = r;
                        break;
                    }
                }

                AddressComponent[] components = result.addressComponents;
                AddressComponentsVo[] componentVos = new AddressComponentsVo[components.length];
                for (int i = 0; i < components.length; i++) {
                    AddressComponentsVo c = new AddressComponentsVo();
                    c.setLongName(components[i].longName);
                    c.setShortName(components[i].shortName);
                    if (components[i].types != null) {
                        String[] types = new String[components[i].types.length];
                        for (int j = 0; j < types.length; j++) {
                            types[j] = components[i].types[j].name();
                        }
                        c.setTypes(types);
                    }
                    componentVos[i] = c;
                }
                ReverseGeocodeVo vo = new ReverseGeocodeVo();
                vo.setAddress(result.formattedAddress);
                vo.setAddressComponentsVos(componentVos);

                String tier1 = null; // point_of_interest, establishment, premise, tourist_attraction
                String tier2 = null; // airport, station, park, natural_feature, shopping_mall, museum, campground
                String tier3 = null; // route
                String tier4 = null; // neighborhood, sublocality, kelurahan/kecamatan (admin level 4/3)
                String tier5 = null; // locality
                String tier6 = null; // administrative_area_level_2, level_1

                for (AddressComponent component : components) {
                    for (AddressComponentType type : component.types) {
                        if (tier1 == null && (
                                type == AddressComponentType.POINT_OF_INTEREST
                                || type == AddressComponentType.ESTABLISHMENT
                                || type == AddressComponentType.PREMISE
                                || type == AddressComponentType.TOURIST_ATTRACTION)) {
                            tier1 = component.longName;
                        }
                        if (tier2 == null && (
                                type == AddressComponentType.AIRPORT
                                || type == AddressComponentType.TRAIN_STATION
                                || type == AddressComponentType.BUS_STATION
                                || type == AddressComponentType.SUBWAY_STATION
                                || type == AddressComponentType.TRANSIT_STATION
                                || type == AddressComponentType.LIGHT_RAIL_STATION
                                || type == AddressComponentType.PARK
                                || type == AddressComponentType.NATURAL_FEATURE
                                || type == AddressComponentType.SHOPPING_MALL
                                || type == AddressComponentType.MUSEUM
                                || type == AddressComponentType.CAMPGROUND)) {
                            tier2 = component.longName;
                        }
                        if (tier3 == null && type == AddressComponentType.ROUTE) {
                            tier3 = component.longName;
                        }
                        if (tier4 == null && (
                                type == AddressComponentType.NEIGHBORHOOD
                                || type == AddressComponentType.SUBLOCALITY_LEVEL_1
                                || type == AddressComponentType.SUBLOCALITY
                                // di Indonesia kelurahan/kecamatan dikirim sebagai admin level 4/3, bukan sublocality
                                || type == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_4
                                || type == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_3)) {
                            tier4 = component.longName;
                        }
                        if (tier5 == null && type == AddressComponentType.LOCALITY) {
                            tier5 = component.longName;
                        }
                        if (tier6 == null && (
                                type == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2
                                || type == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1)) {
                            tier6 = component.longName;
                        }
                    }
                }

                // Reverse geocode hampir tidak pernah berisi komponen POI/establishment;
                // ambil nama tempat sebenarnya dari Places Nearby Search
                if (tier1 == null) {
                    try {
                        PlacesSearchResponse nearby = PlacesApi.nearbySearchQuery(context, new LatLng(lat, lng))
                                .radius(50)
                                .language("id")
                                .await();
                        for (PlacesSearchResult r : nearby.results) {
                            if (isActualPlace(r)) {
                                tier1 = r.name;
                                break;
                            }
                        }
                    } catch (Exception ignored) {
                        // nearby search opsional; tetap pakai hasil reverse geocode jika gagal
                    }
                }

                String name = tier1 != null ? tier1
                        : tier2 != null ? tier2
                        : tier3 != null ? tier3
                        : tier4 != null ? tier4
                        : tier5 != null ? tier5
                        : tier6;

                if (name == null && result.plusCode != null) {
                    name = result.plusCode.compoundCode != null
                            ? result.plusCode.compoundCode
                            : result.plusCode.globalCode;
                }

                vo.setName(name);
                return vo;
            }
            return null;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Nearby Search sering mengembalikan wilayah administratif (kota/kecamatan) atau jalan
     * sebagai hasil pertama; hanya anggap hasil sebagai tempat nyata (POI) jika bukan wilayah/jalan.
     */
    private boolean isActualPlace(PlacesSearchResult r) {
        if (r.types == null || r.name == null) {
            return false;
        }
        for (String type : r.types) {
            if ("political".equals(type)
                    || "route".equals(type)
                    || "locality".equals(type)
                    || "sublocality".equals(type)
                    || "neighborhood".equals(type)
                    || "postal_code".equals(type)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find place from text: exact place search
     *
     * @param input Search input text
     * @return FindPlaceFromTextVo with name, address, lat/lng, or null if not found
     */
    public FindPlaceFromTextVo findplacefromtext(String input) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            FindPlaceFromTextRequest request = new FindPlaceFromTextRequest(context)
                    .input(input)
                    .inputType(FindPlaceFromTextRequest.InputType.TEXT_QUERY)
                    .language("en");
            PlacesSearchResult[] candidates = request.await().candidates;
            if (candidates.length > 0) {
                PlacesSearchResult candidate = candidates[0];
                Geometry geometry = candidate.geometry;
                if (geometry == null) return null;
                FindPlaceFromTextVo vo = new FindPlaceFromTextVo();
                vo.setName(candidate.name);
                vo.setAddress(candidate.formattedAddress);
                vo.setLat(geometry.location.lat);
                vo.setLng(geometry.location.lng);
                return vo;
            }
            return null;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Text search: fuzzy place search by query
     *
     * @param query Search query (e.g. "Indomaret Sudirman Jakarta")
     * @return FindPlaceFromTextVo with top result, or null if not found
     */
    public FindPlaceFromTextVo textsearch(String query) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            PlacesSearchResult[] results = new TextSearchRequest(context)
                    .query(query)
                    .language("en")
                    .await().results;
            if (results.length > 0) {
                PlacesSearchResult result = results[0];
                FindPlaceFromTextVo vo = new FindPlaceFromTextVo();
                vo.setName(result.name);
                vo.setAddress(result.formattedAddress);
                vo.setLat(result.geometry.location.lat);
                vo.setLng(result.geometry.location.lng);
                return vo;
            }
            return null;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Distance matrix: estimated distance and duration between two addresses
     *
     * @param origin      Origin address
     * @param destination Destination address
     * @return DistancematrixVo with distance (meters) and duration (seconds)
     */
    public DistancematrixVo getDistancematrix(String origin, String destination) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, new String[]{origin}, new String[]{destination})
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .departureTime(Instant.now())
                    .language("en")
                    .await();
            DistanceMatrixElement element = matrix.rows[0].elements[0];
            DistancematrixVo vo = new DistancematrixVo();
            vo.setDistance(element.distance.inMeters);
            vo.setDuration(element.duration.inSeconds);
            return vo;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Distance matrix: estimated distance and duration between two coordinates
     *
     * @param sLat Origin latitude
     * @param sLng Origin longitude
     * @param eLat Destination latitude
     * @param eLng Destination longitude
     * @return DistancematrixVo with distance (meters) and duration (seconds)
     */
    public DistancematrixVo getDistancematrix(Double sLat, Double sLng, Double eLat, Double eLng) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                    .origins(new LatLng(sLat, sLng))
                    .destinations(new LatLng(eLat, eLng))
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .departureTime(Instant.now())
                    .language("en")
                    .await();
            DistanceMatrixElement element = matrix.rows[0].elements[0];
            DistancematrixVo vo = new DistancematrixVo();
            vo.setDistance(element.distance.inMeters);
            vo.setDuration(element.duration.inSeconds);
            return vo;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Search places: fuzzy place search returning multiple results.
     * Replaces PhotonUtil.searchPlaces().
     *
     * @param query Search query (e.g. "Indomaret Sudirman Jakarta")
     * @param limit Max number of results (1-20)
     * @param lat   Optional: location bias latitude
     * @param lng   Optional: location bias longitude
     * @return List of FindPlaceFromTextVo, empty list if not found
     */
    public List<FindPlaceFromTextVo> searchPlaces(String query, int limit, Double lat, Double lng, String language, int radius) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            String lang = (language != null && !language.isEmpty()) ? language : "id";
            TextSearchRequest request = new TextSearchRequest(context)
                    .query(query)
                    .language(lang);

            if (lat != null && lng != null) {
                request = request.location(new LatLng(lat, lng)).radius(radius > 0 ? radius : 50000);
            }

            PlacesSearchResult[] results = request.await().results;
            List<FindPlaceFromTextVo> list = new ArrayList<>();
            int count = Math.min(limit > 0 ? limit : 5, results.length);

            for (int i = 0; i < count; i++) {
                PlacesSearchResult r = results[i];
                FindPlaceFromTextVo vo = new FindPlaceFromTextVo();
                vo.setName(r.name);
                vo.setAddress(r.formattedAddress);
                if (r.geometry != null) {
                    vo.setLat(r.geometry.location.lat);
                    vo.setLng(r.geometry.location.lng);
                }
                list.add(vo);
            }

            return list;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Nearby place search: find the most prominent place near a coordinate.
     * Uses Places API Nearby Search (no text query required).
     *
     * @param lat          Latitude
     * @param lng          Longitude
     * @param radiusMeters Search radius in meters (recommended: 50-100)
     * @return FindPlaceFromTextVo with POI name, or null if no place found
     */
    public FindPlaceFromTextVo searchNearbyPlace(double lat, double lng, int radiusMeters) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, new LatLng(lat, lng))
                    .radius(radiusMeters > 0 ? radiusMeters : 100)
                    .language("id")
                    .await();
            PlacesSearchResult[] results = response.results;
            if (results != null) {
                for (PlacesSearchResult r : results) {
                    if (!isActualPlace(r)) {
                        continue;
                    }
                    FindPlaceFromTextVo vo = new FindPlaceFromTextVo();
                    vo.setName(r.name);
                    vo.setAddress(r.formattedAddress != null ? r.formattedAddress : r.vicinity);
                    if (r.geometry != null) {
                        vo.setLat(r.geometry.location.lat);
                        vo.setLng(r.geometry.location.lng);
                    }
                    return vo;
                }
            }
            return null;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Nearby place search filtered by route name + distance: Places Nearby Search result[0]
     * tidak selalu yang terdekat (kadang listing nyasar dari lokasi lain). Untuk mengurangi
     * false-positive, hanya kembalikan POI yang:
     * 1. alamatnya menyebut nama jalan (routeName) dari hasil reverse geocode, DAN
     * 2. koordinatnya berada dalam maxDistanceMeters dari lat,lng input (Haversine)
     *
     * @param lat       Latitude
     * @param lng       Longitude
     * @param radiusMeters Search radius in meters untuk Places Nearby Search (recommended: 50-100)
     * @param routeName Nama jalan dari reverse geocode (address component tipe ROUTE)
     * @param maxDistanceMeters Jarak maksimum POI dari lat,lng input (recommended: 25)
     * @return FindPlaceFromTextVo POI pertama yang cocok, atau null jika tidak ada
     */
    public FindPlaceFromTextVo findNearbyPlaceOnRoute(double lat, double lng, int radiusMeters, String routeName, double maxDistanceMeters) throws Exception {
        if (routeName == null || routeName.trim().isEmpty()) {
            return null;
        }
        String normalizedRoute = normalizeForMatch(routeName);
        if (normalizedRoute.length() < 3) {
            return null;
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, new LatLng(lat, lng))
                    .radius(radiusMeters > 0 ? radiusMeters : 100)
                    .language("id")
                    .await();
            PlacesSearchResult[] results = response.results;
            if (results != null) {
                for (PlacesSearchResult r : results) {
                    if (!isActualPlace(r)) {
                        continue;
                    }
                    String address = r.formattedAddress != null ? r.formattedAddress : r.vicinity;
                    if (address == null || !normalizeForMatch(address).contains(normalizedRoute)) {
                        continue;
                    }
                    if (r.geometry == null) {
                        continue;
                    }
                    double distance = haversineMeters(lat, lng, r.geometry.location.lat, r.geometry.location.lng);
                    if (distance > maxDistanceMeters) {
                        continue;
                    }
                    FindPlaceFromTextVo vo = new FindPlaceFromTextVo();
                    vo.setName(r.name);
                    vo.setAddress(address);
                    vo.setLat(r.geometry.location.lat);
                    vo.setLng(r.geometry.location.lng);
                    return vo;
                }
            }
            return null;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Normalisasi nama jalan/alamat untuk pencocokan: lowercase, hapus karakter non-alfanumerik,
     * dan hapus prefix "jalan"/"jl" supaya "Jalan TB Simatupang" cocok dengan "Jl. T. B. Simatupang No. 19".
     */
    private String normalizeForMatch(String text) {
        String normalized = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        if (normalized.startsWith("jalan")) {
            normalized = normalized.substring("jalan".length());
        } else if (normalized.startsWith("jl")) {
            normalized = normalized.substring("jl".length());
        }
        return normalized;
    }

    /**
     * Jarak antara dua koordinat dalam meter (Haversine formula).
     */
    private double haversineMeters(double lat1, double lng1, double lat2, double lng2) {
        final double earthRadiusMeters = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusMeters * c;
    }

    /**
     * Nearby place search (debug): returns all raw candidates within radius, unfiltered,
     * including their place types. Use this to diagnose why searchNearbyPlace() picked
     * (or skipped) a particular result.
     *
     * @param lat          Latitude
     * @param lng          Longitude
     * @param radiusMeters Search radius in meters (recommended: 50-100)
     * @return List of FindPlaceFromTextVo (with types), empty list if no place found
     */
    public List<FindPlaceFromTextVo> searchNearbyPlaceCandidates(double lat, double lng, int radiusMeters) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, new LatLng(lat, lng))
                    .radius(radiusMeters > 0 ? radiusMeters : 100)
                    .language("id")
                    .await();
            List<FindPlaceFromTextVo> list = new ArrayList<>();
            if (response.results != null) {
                for (PlacesSearchResult r : response.results) {
                    FindPlaceFromTextVo vo = new FindPlaceFromTextVo();
                    vo.setName(r.name);
                    vo.setAddress(r.formattedAddress != null ? r.formattedAddress : r.vicinity);
                    if (r.geometry != null) {
                        vo.setLat(r.geometry.location.lat);
                        vo.setLng(r.geometry.location.lng);
                    }
                    if (r.types != null) {
                        vo.setTypes(r.types.clone());
                    }
                    list.add(vo);
                }
            }
            return list;
        } finally {
            context.shutdown();
        }
    }

    /**
     * Directions: route plan between two locations
     *
     * @param origin      Origin (address, place ID, or lat/lng)
     * @param destination Destination (address, place ID, or lat/lng)
     * @return DirectionsResult containing routes, legs, and steps
     */
    public DirectionsResult getDirections(String origin, String destination) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            return DirectionsApi.getDirections(context, origin, destination)
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .departureTime(Instant.now())
                    .language("en")
                    .await();
        } finally {
            context.shutdown();
        }
    }
}
