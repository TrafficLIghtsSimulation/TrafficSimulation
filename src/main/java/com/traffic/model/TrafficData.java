package com.traffic.model;

import com.traffic.model.enums.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents the traffic density data for each direction.
 * This data is used to calculate green light durations.
 * Singleton TrafficData - global erişim için
 */
public class TrafficData {

    private static final TrafficData instance = new TrafficData();

    private final Map<Direction, Integer> vehicleCounts;
    private boolean isRandom;

    private TrafficData() {
        vehicleCounts = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            vehicleCounts.put(direction, 0);
        }
        isRandom = false;
    }

    public static TrafficData getInstance() {
        return instance;
    }

    public void setVehicleCount(Direction direction, int count) {
        vehicleCounts.put(direction, count);
    }

    public int getVehicleCount(Direction direction) {
        return vehicleCounts.getOrDefault(direction, 0);
    }

    public Map<Direction, Integer> getAllCounts() {
        return vehicleCounts;
    }

    public int getTotalVehicleCount() {
        return vehicleCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void reset() {
        for (Direction direction : vehicleCounts.keySet()) {
            vehicleCounts.put(direction, 0);
        }
        isRandom = false;
    }

    public void setRandom(boolean random) {
        this.isRandom = random;
    }

    public boolean isRandom() {
        return isRandom;
    }
}
