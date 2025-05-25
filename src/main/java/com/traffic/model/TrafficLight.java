package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.LightState;

/**
 * Represents a traffic light for a given direction.
 * Holds the current light state, durations, and basic state logic.
 */
public class TrafficLight {
    private final Direction direction;
    private LightState currentState;
    private int greenDuration;
    private final int yellowDuration = 3;

    public TrafficLight(Direction direction) {
        this.direction = direction;
        this.currentState = LightState.RED;
    }

    public Direction getDirection() {
        return direction;
    }

    public LightState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(LightState state) {

        this.currentState = state;
    }

    public int getGreenDuration() {
        return greenDuration;
    }

    public void setGreenDuration(int greenDuration) {
        this.greenDuration = greenDuration;
    }

    public int getYellowDuration() {
        return yellowDuration;
    }

    public int getRedDuration() {
        return 120 - (greenDuration + yellowDuration);
    }

    public boolean isGreen() {
        return currentState == LightState.GREEN;
    }

    public boolean isYellow() {
        return currentState == LightState.YELLOW;
    }

    public boolean isRed() {
        return currentState == LightState.RED;
    }
}
