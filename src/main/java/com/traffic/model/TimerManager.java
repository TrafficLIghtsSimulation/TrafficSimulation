package com.traffic.model;
import com.traffic.model.enums.Direction;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages timing and scheduling of light changes.
 * Typically used to progress simulation time and switch light states.
 */
public class TimerManager {

    private final Map<Direction, Integer> greenTimes;
    private final Map<Direction, Integer> yellowTimes;

    private int elapsedTime;
    private int cycleTime;

    public TimerManager(Map<Direction, Integer> greenDurations) {
        this.greenTimes = new HashMap<>(greenDurations);
        this.yellowTimes = new HashMap<>();
        for (Direction dir : greenDurations.keySet()) {
            yellowTimes.put(dir, 3); // fixed yellow time
        }
        this.elapsedTime = 0;
        this.cycleTime = 120; // total cycle time
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public int getGreenTime(Direction direction) {
        return greenTimes.getOrDefault(direction, 0);
    }

    public int getYellowTime(Direction direction) {
        return yellowTimes.getOrDefault(direction, 3);
    }

    public int getRedTime(Direction direction) {
        int green = getGreenTime(direction);
        int yellow = getYellowTime(direction);
        return cycleTime - (green + yellow);
    }

}