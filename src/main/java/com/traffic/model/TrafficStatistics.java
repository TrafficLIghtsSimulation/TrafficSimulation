package com.traffic.model;
/**
 * Tracks statistics for the simulation, such as total passed and waiting vehicles.
 */
public class TrafficStatistics {

    private int totalPassed;
    private int totalWaiting;

    public TrafficStatistics() {
        this.totalPassed = 0;
        this.totalWaiting = 0;
    }

    public void incrementPassed() {
        totalPassed++;
    }

    public void incrementPassed(int count) {
        totalPassed += count;
    }

    public void incrementWaiting() {
        totalWaiting++;
    }

    public void incrementWaiting(int count) {
        totalWaiting += count;
    }

    public int getTotalPassed() {
        return totalPassed;
    }

    public int getTotalWaiting() {
        return totalWaiting;
    }

    public int getTotalVehicles() {
        return totalPassed + totalWaiting;
    }

    public void reset() {
        totalPassed = 0;
        totalWaiting = 0;
    }
}
