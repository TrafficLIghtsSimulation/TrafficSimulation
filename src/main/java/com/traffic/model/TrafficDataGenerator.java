package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;

import java.util.*;

/**
 * Generates traffic vehicle queues for each direction.
 * - The number of vehicles per direction is given via TrafficData
 * - The type and movement of each vehicle is generated randomly
 */
public class TrafficDataGenerator {

    private static final List<Class<? extends Vehicle>> vehicleTypes = Arrays.asList(
            Car.class, Bus.class, Truck.class, Motorcycle.class
    );

    private static final Random random = new Random();

    // Her yönde, araç sayısı kadar,rastgele tür (Car, Bus, Truck, Motorcycle) ve rastgele yönde  (Forward, Left, Right) özelliklerinde araç üretir

    public static Map<Direction, Queue<Vehicle>> generateTrafficFromCounts(Map<Direction, Integer> vehicleCounts) {
        Map<Direction, Queue<Vehicle>> trafficMap = new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            Queue<Vehicle> queue = new LinkedList<>();
            int numberOfVehicles = vehicleCounts.getOrDefault(direction, 0);

            for (int i = 0; i < numberOfVehicles; i++) {
                Class<? extends Vehicle> type = vehicleTypes.get(random.nextInt(vehicleTypes.size()));
                MovementType movement = MovementType.values()[random.nextInt(MovementType.values().length)];

                Vehicle vehicle = VehicleFactory.createVehicle(
                        type,
                        generateRandomId(type),
                        direction,
                        movement,
                        3.0 + random.nextDouble() * 2
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
