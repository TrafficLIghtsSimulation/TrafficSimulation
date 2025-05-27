package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.interfaces.IVehicleStatisticsListener;

import java.util.Map;
import java.util.Queue;

/**
 * Coordinates traffic simulation state and logic.
 * Manages vehicle queues and statistics updates.
 */
public class SimulationManager {

    private final Intersection intersection;
    private final TrafficStatistics statistics;
    private final Map<Direction, Queue<Vehicle>> trafficQueues;
    private IVehicleStatisticsListener statsListener;

    public SimulationManager(Intersection intersection,
                             TrafficStatistics statistics,
                             Map<Direction, Queue<Vehicle>> trafficQueues) {
        this.intersection = intersection;
        this.statistics = statistics;
        this.trafficQueues = trafficQueues;
    }

    public void step(Direction activeDirection) {
        Queue<Vehicle> queue = trafficQueues.get(activeDirection);
        TrafficLight light = intersection.getTrafficLight(activeDirection);

        if (light.isGreen() && queue != null && !queue.isEmpty()) {
            Vehicle vehicle = queue.poll(); // one vehicle passed
            statistics.incrementPassed(activeDirection);
        }

        // remaining vehicles are considered waiting
        int waiting = (queue != null) ? queue.size() : 0;
        statistics.incrementWaiting(activeDirection, waiting);

        notifyStatisticsListener();
    }

    public void reset() {
        statistics.reset();
        for (Queue<Vehicle> queue : trafficQueues.values()) {
            queue.clear();
        }
        notifyStatisticsListener();
    }

    public void setStatsListener(IVehicleStatisticsListener listener) {
        this.statsListener = listener;
    }

    private void notifyStatisticsListener() {
        if (statsListener != null) {
            statsListener.onStatsUpdated(
                    statistics.getTotalPassed(),
                    statistics.getTotalWaiting()
            );
        }
    }

    public Map<Direction, Queue<Vehicle>> getTrafficQueues() {
        return trafficQueues;
    }
}