package com.traffic.model;
import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;
import com.traffic.model.enums.LightState;

/**
 * Abstract representation of a vehicle in the simulation.
 * Includes position, direction, speed, and motion behavior near traffic lights.
 */
public abstract class Vehicle {

    protected String id;
    protected Direction direction;
    protected MovementType movementType;
    protected double speed;
    protected double originalSpeed;
    protected double brakingDistance = 20.0; // distance before the light to start slowing down
    protected boolean stopped;

    protected double posX;
    protected double posY;

    public Vehicle(String id, Direction direction, MovementType movementType, double speed) {
        this.id = id;
        this.direction = direction;
        this.movementType = movementType;
        this.speed = speed;
        this.originalSpeed = speed;
        this.stopped = false;
    }

    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }


    public String getId() {
        return id;
    }


    public Direction getDirection() {
        return direction;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void move(TrafficLight light, double stopLineX, double stopLineY) {
        boolean approaching = false;

        switch (direction) {
            case NORTH:
                approaching = (posY - stopLineY) <= brakingDistance;
                break;
            case SOUTH:
                approaching = (stopLineY - posY) <= brakingDistance;
                break;
            case EAST:
                approaching = (stopLineX - posX) <= brakingDistance;
                break;
            case WEST:
                approaching = (posX - stopLineX) <= brakingDistance;
                break;
        }

        if (light.getCurrentState() == LightState.RED && approaching) {
            if (speed > 0) {
                speed = Math.max(speed - 0.1, 0);
            }
            if (speed == 0) {
                stopped = true;
            }
        } else {
            if (stopped && light.getCurrentState() == LightState.GREEN) {
                speed = originalSpeed;
                stopped = false;
            }

            switch (direction) {
                case NORTH:
                    posY -= speed;
                    break;
                case SOUTH:
                    posY += speed;
                    break;
                case EAST:
                    posX += speed;
                    break;
                case WEST:
                    posX -= speed;
                    break;
            }
        }
    }

    public abstract double getLength();
}
