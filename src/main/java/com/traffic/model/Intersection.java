package com.traffic.model;

import com.traffic.model.enums.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a four-way traffic intersection.
 * Holds TrafficLight objects for each direction.
 */
public class Intersection {

    private final Map<Direction, TrafficLight> lights;

    public Intersection() {
        lights = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            lights.put(dir, new TrafficLight(dir));
        }
    }

    public TrafficLight getTrafficLight(Direction direction) {
        return lights.get(direction);
    }

    public void setGreenDuration(Direction direction, int duration) {
        TrafficLight light = lights.get(direction);
        if (light != null) {
            light.setGreenDuration(duration);
        }
    }
}