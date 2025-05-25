package com.traffic.model.interfaces;

public interface IVehicleStatisticsListener {
    void onStatsUpdated(int totalPassed, int totalWaiting);
}
