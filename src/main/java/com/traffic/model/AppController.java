package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.interfaces.ISignalCalculationStrategy;

import java.util.Map;
import java.util.Queue;

/**
 * Entry point and orchestrator for the simulation.
 * Coordinates setup and connects model components.
 */
public class AppController {

    private final Intersection intersection;
    private final SimulationManager simulationManager;
    private final TimerManager timerManager;
    private final TrafficStatistics statistics;

    public AppController(Map<Direction, Integer> trafficCounts,
                         ISignalCalculationStrategy signalStrategy) {
        this.intersection = new Intersection();
        this.statistics = TrafficStatistics.getInstance();

        // Trafik kuyruklarını otomatik üretiyoruz:
        Map<Direction, Queue<Vehicle>> trafficQueues =
                TrafficDataGenerator.generateTrafficFromCounts(trafficCounts);

        this.simulationManager = new SimulationManager(intersection, statistics, trafficQueues);

        Map<Direction, Integer> greenDurations = signalStrategy.calculateGreenLightDurations(trafficCounts);
        for (Direction direction : greenDurations.keySet()) {
            intersection.setGreenDuration(direction, greenDurations.get(direction));
        }

        this.timerManager = new TimerManager(greenDurations);
    }

    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public TrafficStatistics getStatistics() {
        return statistics;
    }
}
