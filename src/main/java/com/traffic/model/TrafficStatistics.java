package com.traffic.model;

import com.traffic.model.enums.Direction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Singleton design pattern used
 * Tracks statistics for the simulation, such as total passed and waiting vehicles.
 */
public class TrafficStatistics {

    private static TrafficStatistics instance;

    private final Map<Direction, TrafficStatisticsEntry> entries;
    private final ObservableList<TrafficStatisticsEntry> observableList;


    private TrafficStatistics() {
        entries = new EnumMap<>(Direction.class);
        observableList = FXCollections.observableArrayList();

        for (Direction dir : Direction.values()) {
            TrafficStatisticsEntry entry = new TrafficStatisticsEntry(dir);
            entries.put(dir, entry);
            observableList.add(entry);
        }
    }

    public static TrafficStatistics getInstance() {
        if (instance == null) {
            instance = new TrafficStatistics();
        }
        return instance;
    }

    public ObservableList<TrafficStatisticsEntry> getObservableList() {
        return observableList;
    }

    public int getTotalPassed() {
        return entries.values().stream().mapToInt(TrafficStatisticsEntry::getPassed).sum();
    }

    public int getTotalWaiting() {
        return entries.values().stream().mapToInt(TrafficStatisticsEntry::getRemaining).sum();
    }

    public void update(Direction dir, int passed, int remaining) {
        TrafficStatisticsEntry entry = entries.get(dir);
        entry.setPassed(passed);
        entry.setRemaining(remaining);
    }

    public void incrementPassed(Direction dir) {
        TrafficStatisticsEntry entry = entries.get(dir);
        entry.setPassed(entry.getPassed() + 1);
    }

    public void incrementWaiting(Direction dir, int count) {
        TrafficStatisticsEntry entry = entries.get(dir);
        entry.setRemaining(count);
    }

    public void reset() {
        for (TrafficStatisticsEntry entry : entries.values()) {
            entry.setPassed(0);
            entry.setRemaining(0);
        }
    }
}

