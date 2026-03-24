package com.dsh.common.map.osrm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * OSRM (Open Source Routing Machine)
 *
 * @author saptariwijaya4@gmail.com
 * @date 2026-01-10
 */
@Slf4j
@Component
public class OSRMUtil {

    // OSRM Server URL - Self-hosted OSRM on localhost
    private static final String OSRM_SERVER_URL = "http://127.0.0.1:5000";

    public OSRMUtil() {
        log.info("[OSRM] - OSRMUtil initialized with server: {}", OSRM_SERVER_URL);
    }

    /**
     * getDistance
     *
     * @param startLat Start point latitude
     * @param startLng Start point longitude
     * @param endLat End point latitude
     * @param endLng End point longitude
     * @return Distance in kilometers, or null if failed
     */
    public static Double getDistance(Double startLat, Double startLng, Double endLat, Double endLng) {
        try {
            log.info("[OSRM] - Calculating distance - start:[{},{}], end:[{},{}]",
                     startLat, startLng, endLat, endLng);

            OSRMRouteResult routeResult = getRoute(startLat, startLng, endLat, endLng);

            if (routeResult != null && routeResult.getDistance() != null) {
                Double distanceKm = routeResult.getDistance() / 1000.0; // Convert meters to kilometers
                log.info("[OSRM] - Distance calculated: {} km", distanceKm);
                return distanceKm;
            }

            log.error("[OSRM] - Failed to calculate distance - null result");
            return null;

        } catch (Exception e) {
            log.error("[OSRM] - Error calculating distance: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * getRoute
     *
     * @param startLat Start point latitude
     * @param startLng Start point longitude
     * @param endLat End point latitude
     * @param endLng End point longitude
     * @return OSRMRouteResult object containing distance, duration, and geometry; or null if failed
     */
    public static OSRMRouteResult getRoute(Double startLat, Double startLng, Double endLat, Double endLng) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            // OSRM Route API endpoint
            // Format: /route/v1/{profile}/{coordinates}?overview={full|simplified|false}&geometries={polyline|polyline6|geojson}
            String urlString = String.format(
                "%s/route/v1/driving/%s,%s;%s,%s?overview=full&geometries=geojson",
                OSRM_SERVER_URL,
                startLng, startLat,  // OSRM uses lng,lat order (NOT lat,lng!)
                endLng, endLat
            );

            log.debug("[OSRM] - Request URL: {}", urlString);

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000); // 10 seconds
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                log.debug("[OSRM] - Response: {}", response.toString());

                // Parse OSRM response
                JSONObject jsonResponse = JSON.parseObject(response.toString());
                String code = jsonResponse.getString("code");

                if ("Ok".equals(code)) {
                    JSONArray routes = jsonResponse.getJSONArray("routes");

                    if (routes != null && routes.size() > 0) {
                        JSONObject route = routes.getJSONObject(0);

                        OSRMRouteResult result = new OSRMRouteResult();
                        result.setDistance(route.getDouble("distance"));  // meters
                        result.setDuration(route.getDouble("duration"));  // seconds

                        // Get geometry (route coordinates)
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

            } else {
                log.error("[OSRM] - HTTP error code: {}", responseCode);
                return null;
            }

        } catch (Exception e) {
            log.error("[OSRM] - Error getting route: {}", e.getMessage(), e);
            return null;

        } finally {
            try {
                if (reader != null) reader.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                log.warn("[OSRM] - Error closing connection: {}", e.getMessage());
            }
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
        final int EARTH_RADIUS_KM = 6371; // Earth's radius in kilometers

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
    public static boolean checkServerHealth() {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(OSRM_SERVER_URL + "/route/v1/driving/0,0;0,0");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();

            boolean isHealthy = (responseCode == 200 || responseCode == 400); // 400 means server is up but invalid coords
            log.info("[OSRM] - Server health check: {}", isHealthy ? "HEALTHY" : "DOWN");

            return isHealthy;

        } catch (Exception e) {
            log.error("[OSRM] - Server health check FAILED: {}", e.getMessage());
            return false;

        } finally {
            if (conn != null) conn.disconnect();
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
