package com.campus.resource;

import com.campus.exception.LinkedResourceNotFoundException;
import com.campus.exception.SensorUnavailableException;
import com.campus.model.Sensor;
import com.campus.model.SensorReading;
import com.campus.store.DataStore;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor not found for this readings path.");
        }

        List<SensorReading> readings = DataStore.getOrCreateReadings(sensorId);
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Cannot add reading: sensor does not exist.");
        }

        if (Sensor.STATUS_MAINTENANCE.equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Cannot add reading while sensor is in MAINTENANCE.");
        }

        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        DataStore.getOrCreateReadings(sensorId).add(reading);
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}
