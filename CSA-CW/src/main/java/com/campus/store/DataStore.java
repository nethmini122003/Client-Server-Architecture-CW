package com.campus.store;

import com.campus.model.Room;
import com.campus.model.Sensor;
import com.campus.model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataStore {
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> sensorReadings = new HashMap<>();

    private DataStore() {
    }

    public static List<SensorReading> getOrCreateReadings(String sensorId) {
        return sensorReadings.computeIfAbsent(sensorId, key -> new ArrayList<>());
    }
}
