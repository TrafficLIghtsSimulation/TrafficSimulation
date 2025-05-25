package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;

import java.util.*;

/**
 * Generates random traffic vehicle queues for each direction.
 * This can be used to simulate random traffic density.
 */
public class TrafficDataGenerator {

    private static final List<Class<? extends Vehicle>> vehicleTypes = Arrays.asList(
            Car.class, Bus.class, Truck.class, Motorcycle.class
    );

    private static final Random random = new Random();

    public static Map<Direction, Queue<Vehicle>> generateRandomTrafficData() {
        Map<Direction, Queue<Vehicle>> trafficMap = new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            Queue<Vehicle> queue = new LinkedList<>();
            int numberOfVehicles = 2 + random.nextInt(5); // 2 to 6 vehicles

            for (int i = 0; i < numberOfVehicles; i++) {
                Class<? extends Vehicle> type = vehicleTypes.get(random.nextInt(vehicleTypes.size()));
                MovementType movement = MovementType.values()[random.nextInt(MovementType.values().length)];

                Vehicle vehicle = VehicleFactory.createVehicle(
                        type,
                        generateRandomId(type),
                        direction,
                        movement,
                        3.0 + random.nextDouble() * 2 // speed between 3.0 and 5.0
                );

                if (vehicle != null) {
                    queue.add(vehicle);
                }
            }

            trafficMap.put(direction, queue);
        }

        return trafficMap;
    }

    private static String generateRandomId(Class<? extends Vehicle> type) {
        return type.getSimpleName().substring(0, 1).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 4);
    }
}