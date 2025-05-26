package com.traffic.model;

import com.traffic.model.enums.Direction;
import javafx.beans.property.*;

public class TrafficStatisticsEntry {
    private final StringProperty direction;
    private final IntegerProperty passed;
    private final IntegerProperty remaining;

    public TrafficStatisticsEntry(Direction direction) {
        this.direction = new SimpleStringProperty(direction.name());
        this.passed = new SimpleIntegerProperty(0);
        this.remaining = new SimpleIntegerProperty(0);
    }

    public StringProperty directionProperty() {
        return direction;
    }

    public IntegerProperty passedProperty() {
        return passed;
    }

    public IntegerProperty remainingProperty() {
        return remaining;
    }

    public String getDirection() {
        return direction.get();
    }

    public void setDirection(String direction) {
        this.direction.set(direction);
    }

    public int getPassed() {
        return passed.get();
    }

    public void setPassed(int passed) {
        this.passed.set(passed);
    }

    public int getRemaining() {
        return remaining.get();
    }

    public void setRemaining(int remaining) {
        this.remaining.set(remaining);
    }
}
