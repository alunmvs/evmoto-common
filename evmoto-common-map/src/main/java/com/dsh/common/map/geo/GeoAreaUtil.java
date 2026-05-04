package com.dsh.common.map.geo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Utility to check whether a geographic point is inside a working area
 * defined in t_open_city.content (polygon or circle shapes).
 *
 * Content format:
 *   Polygon: {"id":..., "type":"polygon", "center":["lat,lon", "lat,lon", ...]}
 *   Circle:  {"id":..., "type":"circle",  "center":"lat,lon", "radius":<meters>}
 */
public class GeoAreaUtil {

    private static final double EARTH_RADIUS_METERS = 6371000.0;

    /**
     * Returns true if (lat, lon) is inside any shape defined in the content JSON.
     * Returns false if content is null/empty or point is outside all shapes.
     */
    public static boolean isInArea(double lat, double lon, String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }
        try {
            JSONArray shapes = JSONArray.parseArray(content);
            for (int i = 0; i < shapes.size(); i++) {
                JSONObject shape = shapes.getJSONObject(i);
                String type = shape.getString("type");
                if ("polygon".equalsIgnoreCase(type)) {
                    if (isInPolygon(lat, lon, shape.getJSONArray("center"))) {
                        return true;
                    }
                } else if ("circle".equalsIgnoreCase(type)) {
                    if (isInCircle(lat, lon, shape.getString("center"), shape.getDoubleValue("radius"))) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Ray-casting algorithm: returns true if (lat, lon) is inside the polygon
     * defined by the given array of "lat,lon" strings.
     */
    private static boolean isInPolygon(double lat, double lon, JSONArray centerArray) {
        if (centerArray == null || centerArray.size() < 3) {
            return false;
        }
        int n = centerArray.size();
        boolean inside = false;
        double[] prevPoint = parseLatLon(centerArray.getString(n - 1));
        for (int i = 0; i < n; i++) {
            double[] currPoint = parseLatLon(centerArray.getString(i));
            double yi = prevPoint[0], xi = prevPoint[1];
            double yj = currPoint[0], xj = currPoint[1];
            if (((yi > lat) != (yj > lat)) &&
                    (lon < (xj - xi) * (lat - yi) / (yj - yi) + xi)) {
                inside = !inside;
            }
            prevPoint = currPoint;
        }
        return inside;
    }

    /**
     * Returns true if (lat, lon) is within radiusMeters of the circle center.
     * Uses Haversine formula.
     */
    private static boolean isInCircle(double lat, double lon, String centerStr, double radiusMeters) {
        double[] center = parseLatLon(centerStr);
        double distance = haversineDistance(lat, lon, center[0], center[1]);
        return distance <= radiusMeters;
    }

    /**
     * Haversine distance in meters between two lat/lon points.
     */
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS_METERS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /** Parses "lat,lon" string into double[]{lat, lon}. */
    private static double[] parseLatLon(String latLon) {
        String[] parts = latLon.split(",");
        return new double[]{Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())};
    }
}
