package com.traffic.model;
import com.traffic.model.enums.Direction;
import com.traffic.model.interfaces.IVehicleDirectionStrategy;

import java.util.Random;

public class RandomDirectionStrategy implements IVehicleDirectionStrategy {
    private final Random random = new Random();

    @Override
    public Direction determineDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }
}

