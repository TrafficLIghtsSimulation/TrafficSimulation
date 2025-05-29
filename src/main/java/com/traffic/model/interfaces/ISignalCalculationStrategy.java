package com.traffic.model.interfaces;
import java.util.Map;
import com.traffic.model.enums.Direction;

public interface ISignalCalculationStrategy {
    Map<Direction, Integer> calculateGreenLightDurations(Map<Direction, Integer> trafficData);
}
