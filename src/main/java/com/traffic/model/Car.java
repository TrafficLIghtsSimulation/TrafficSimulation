package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;

/**
 * A specific type of Vehicle representing a car.
 */
public class Car extends Vehicle {

    public Car(String id, Direction direction, MovementType movementType, double speed) {
        super(id, direction, movementType, speed);
    }

}
