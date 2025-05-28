package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;

public class Truck extends Vehicle {

    public Truck(String id, Direction direction, MovementType movementType, double speed) {
        super(id, direction, movementType, speed);
    }

    //move() metodu vehicle da olduğu gibi çalışır
}
