package com.dsh.common.map.osrm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

/**
 * OSRM (Open Source Routing Machine)
 *
 * @author saptariwijaya4@gmail.com
 * @date 2026-01-10
 */
@Slf4j
@Component
public class OSRMUtil {

    private static String OSRM_SERVER_URL;

    // BUG-T4: singleton dengan connection pool — tidak buat koneksi TCP baru setiap call
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
            .build();

    public OSRMUtil(@Value("${map.osrm.server-url}") String osrmServerUrl) {
        OSRM_SERVER_URL = osrmServerUrl;
        log.info("[OSRM] - OSRMUtil initialized with server: {}", OSRM_SERVER_URL);
    }

    /**
     * getDistance
     *
     * @param startLat Start point latitude
     * @param startLng Start point longitude
     * @param endLat End point latitude
     * @param endLng End point longitude
     * @return Distance in kilometers, or null if route not found
     */
    public static Double getDistance(Double startLat, Double startLng, Double endLat, Double endLng) throws Exception {
        log.info("[OSRM] - Calculating distance - start:[{},{}], end:[{},{}]",
                 startLat, startLng, endLat, endLng);

        OSRMRouteResult routeResult = getRoute(startLat, startLng, endLat, endLng);

        if (routeResult != null && routeResult.getDistance() != null) {
            Double distanceKm = routeResult.getDistance() / 1000.0;
            log.info("[OSRM] - Distance calculated: {} km", distanceKm);
            return distanceKm;
        }

        return null;
    }

    /**
     * getRoute
     *
     * @param startLat Start point latitude
     * @param startLng Start point longitude
     * @param endLat End point latitude
     * @param endLng End point longitude
     * @return OSRMRouteResult object containing distance, duration, and geometry; or null if not found
     */
    public static OSRMRouteResult getRoute(Double startLat, Double startLng, Double endLat, Double endLng) throws Exception {
        String urlString = String.format(
            "%s/route/v1/driving/%s,%s;%s,%s?overview=full&geometries=geojson",
            OSRM_SERVER_URL,
            startLng, startLat,  // OSRM uses lng,lat order (NOT lat,lng!)
            endLng, endLat
        );

        log.debug("[OSRM] - Request URL: {}", urlString);

        Request request = new Request.Builder().url(urlString).build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("[OSRM] - HTTP error code: {}", response.code());
                return null;
            }

            String responseBody = response.body().string();
            log.debug("[OSRM] - Response: {}", responseBody);

            JSONObject jsonResponse = JSON.parseObject(responseBody);
            String code = jsonResponse.getString("code");

            if ("Ok".equals(code)) {
                JSONArray routes = jsonResponse.getJSONArray("routes");
                if (routes != null && routes.size() > 0) {
                    JSONObject route = routes.getJSONObject(0);

                    OSRMRouteResult result = new OSRMRouteResult();
                    result.setDistance(route.getDouble("distance"));
                    result.setDuration(route.getDouble("duration"));

                    JSONObject geometry = route.getJSONObject("geometry");
                    if (geometry != null) {
                        result.setGeometry(geometry.toJSONString());
                    }

                    log.info("[OSRM] - Route calculated - distance: {}m, duration: {}s",
                             result.getDistance(), result.getDuration());
                    return result;
                }
            }

            log.error("[OSRM] - API returned non-Ok code: {}", code);
            return null;
        }
    }

    /**
     * Haversine distance calculation between two geographic points
     *
     * @param lat1 Point 1 latitude
     * @param lng1 Point 1 longitude
     * @param lat2 Point 2 latitude
     * @param lng2 Point 2 longitude
     * @return Distance in kilometers
     */
    public static Double getHaversineDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        log.debug("[OSRM] - Haversine distance: {} km", distance);

        return distance;
    }

    /**
     * Check OSRM server health
     *
     * @return true if server is accessible
     */
    public static boolean checkServerHealth() throws Exception {
        Request request = new Request.Builder()
                .url(OSRM_SERVER_URL + "/route/v1/driving/0,0;0,0")
                .build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            boolean isHealthy = (response.code() == 200 || response.code() == 400);
            log.info("[OSRM] - Server health check: {}", isHealthy ? "HEALTHY" : "DOWN");
            return isHealthy;
        }
    }

    /**
     * OSRM Route result class
     */
    public static class OSRMRouteResult {
        private Double distance;  // Distance in meters
        private Double duration;  // Duration in seconds
        private String geometry;  // GeoJSON geometry string

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public Double getDuration() {
            return duration;
        }

        public void setDuration(Double duration) {
            this.duration = duration;
        }

        public String getGeometry() {
            return geometry;
        }

        public void setGeometry(String geometry) {
            this.geometry = geometry;
        }

        public Double getDistanceKm() {
            return distance != null ? distance / 1000.0 : null;
        }

        public Double getDurationMinutes() {
            return duration != null ? duration / 60.0 : null;
        }
    }
}
