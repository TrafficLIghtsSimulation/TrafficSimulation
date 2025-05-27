package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;
import java.lang.reflect.Constructor;

public class VehicleFactory {

    /**
     * Creates a specific vehicle (Car, Truck, Bus, Motorcycle) by passing the class reference.
     *
     * @param vehicleClass The class of the vehicle to create (e.g., Car.class)
     * @param id           Unique identifier for the vehicle
     * @param direction    Direction the vehicle is heading
     * @param movementType Movement type (FORWARD, LEFT, RIGHT)
     * @param speed        Speed of the vehicle
     * @return A new instance of the specified Vehicle subclass
     */
    public static Vehicle createVehicle(Class<? extends Vehicle> vehicleClass,
                                        String id,
                                        Direction direction,
                                        MovementType movementType,
                                        double speed) {
        try {
            // 1. İlgili constructor'ı bul
            Constructor<? extends Vehicle> constructor = vehicleClass.getDeclaredConstructor(
                    String.class, Direction.class, MovementType.class, double.class
            );

            // 2. Constructor ile yeni nesne oluştur
            Vehicle vehicle = constructor.newInstance(id, direction, movementType, speed);

            // 3. Nesneyi döndür
            return vehicle;

        } catch (Exception e) {
            System.err.println("Vehicle creation failed: " + e.getMessage());
            return null;
        }
    }

    //Araç türlerinin resimler ile eşleştirilmesi
    public static String getVehicleImagePath(Class<? extends Vehicle> vehicleClass) {
        if (vehicleClass == Truck.class) return "/com/traffic/view/main_assets/truck.png";
        if (vehicleClass == Bus.class) return "/com/traffic/view/main_assets/bus.png";
        if (vehicleClass == Car.class) return "/com/traffic/view/main_assets/car.png";
        if (vehicleClass == Motorcycle.class) return "/com/traffic/view/main_assets/motorcycle.png";
        return null;
    }

    public static class IntersectionPane {
    }

    public static class MainView {
    }

    public static class StatisticsPanel {
    }

    public static class VehicleNode {
    }
}