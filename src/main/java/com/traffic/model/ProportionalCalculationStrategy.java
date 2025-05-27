package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.interfaces.ISignalCalculationStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * This strategy calculates green light durations proportionally based on traffic density.
 *
 * Total cycle time: 120 seconds (fixed)
 * - Each direction gets a fixed yellow duration (e.g., 3 seconds)
 * - Remaining time is distributed proportionally as green light time
 * - Green light can be 0 if traffic is 0
 * - Ensures that green + yellow for all directions equals 120 seconds
 */
public class ProportionalCalculationStrategy implements ISignalCalculationStrategy {
    // 0-60 SANİYE ARASI MAX YEŞİL IŞIK SÜRESİ ATAMASI


    private static final int TOTAL_CYCLE = 120;            // Full cycle time in seconds
    private static final int YELLOW_DURATION = 3;          // Fixed yellow duration per direction

    @Override
    public Map<Direction, Integer> calculateGreenLightDurations(Map<Direction, Integer> trafficData) {
        Map<Direction, Integer> greenDurations = new HashMap<>();
        int numberOfDirections = trafficData.size();
        int totalGreenTime = TOTAL_CYCLE - (YELLOW_DURATION * numberOfDirections);

        int totalVehicles = trafficData.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        for (Map.Entry<Direction, Integer> entry : trafficData.entrySet()) {
            Direction direction = entry.getKey();
            int vehicleCount = entry.getValue();

            // Calculate proportional share of green time
            double proportion = (totalVehicles == 0)
                    ? 1.0 / numberOfDirections
                    : vehicleCount / (double) totalVehicles;

            // Green time is the proportional share of available green time
            int green = (int) Math.round(proportion * totalGreenTime);

            // Ensure no negative green time
            green = Math.max(0, green);

            greenDurations.put(direction, green);
        }

        return greenDurations;
    }
}