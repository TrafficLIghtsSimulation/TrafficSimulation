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

    //move() metodu vehicle da olduğu gibi çalışır

    @Override
    public double getLength() {
        return 4.0; // typical car length in meters
    }
}
