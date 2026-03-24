package com.dsh.common.map.google;

import com.dsh.common.map.vo.AddressComponentsVo;
import com.dsh.common.map.vo.DistancematrixVo;
import com.dsh.common.map.vo.FindPlaceFromTextVo;
import com.dsh.common.map.vo.GeocodeVo;
import com.dsh.common.map.vo.ReverseGeocodeVo;
import com.google.maps.*;
import com.google.maps.model.*;
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

    private final String key = "AIzaSyBNgMRPVj8zt4OEGrUoyHifEDE9SCHYSLE";

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
                    .language("en")
                    .await();
            if (results.length > 0) {
                AddressComponent[] components = results[0].addressComponents;
                AddressComponentsVo[] componentVos = new AddressComponentsVo[components.length];
                for (int i = 0; i < components.length; i++) {
                    AddressComponentsVo c = new AddressComponentsVo();
                    c.setLongName(components[i].longName);
                    c.setShortName(components[i].shortName);
                    componentVos[i] = c;
                }
                ReverseGeocodeVo vo = new ReverseGeocodeVo();
                vo.setAddress(results[0].formattedAddress);
                vo.setAddressComponentsVos(componentVos);
                return vo;
            }
            return null;
        } finally {
            context.shutdown();
        }
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
    public List<FindPlaceFromTextVo> searchPlaces(String query, int limit, Double lat, Double lng) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        try {
            TextSearchRequest request = new TextSearchRequest(context)
                    .query(query)
                    .language("en");

            if (lat != null && lng != null) {
                request = request.location(new LatLng(lat, lng));
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
