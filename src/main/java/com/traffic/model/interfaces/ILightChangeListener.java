package com.traffic.model.interfaces;
import com.traffic.model.enums.Direction;
import com.traffic.model.enums.LightState;
public interface ILightChangeListener {
    void onLightChanged(Direction direction, LightState newState);
}
