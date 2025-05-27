package com.traffic.model;

import com.traffic.model.enums.Direction;
import javafx.geometry.Point2D;

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

    public Map<Direction, TrafficLight> getAllTrafficLights() {
        return lights;
    }

    public void resetAllLightsToRed() {
        for (TrafficLight light : lights.values()) {
            light.setCurrentState(com.traffic.model.enums.LightState.RED);
        }
    }
    //kavşağa girip girmediğimin kontrolü için
    public static Point2D getIntersectionPoint(Direction direction){
        switch (direction) {
            case NORTH: return new Point2D(0, 0);
            case SOUTH: return new Point2D(0, 1);
            case EAST: return new Point2D(1, 0);
            case WEST: return new Point2D(1, 1);
            default: return null;
        }
    }
}