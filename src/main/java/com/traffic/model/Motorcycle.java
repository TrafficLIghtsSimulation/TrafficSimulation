package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;

public class Motorcycle extends Vehicle {

    public Motorcycle(String id, Direction direction, MovementType movementType, double speed) {
        super(id, direction, movementType, speed);
    }
}
