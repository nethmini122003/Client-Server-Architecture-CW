package com.campus.resource;

import com.campus.exception.RoomNotEmptyException;
import com.campus.model.ErrorResponse;
import com.campus.model.Room;
import com.campus.store.DataStore;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Response getAllRooms() {
        List<Room> rooms = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(rooms).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isBlank()) {
            room.setId(UUID.randomUUID().toString());
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            ErrorResponse error = new ErrorResponse(
                    Response.Status.NOT_FOUND.getStatusCode(),
                    Response.Status.NOT_FOUND.getReasonPhrase(),
                    "Room not found."
            );
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            ErrorResponse error = new ErrorResponse(
                    Response.Status.NOT_FOUND.getStatusCode(),
                    Response.Status.NOT_FOUND.getReasonPhrase(),
                    "Room not found."
            );
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because it still has assigned sensors.");
        }

        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}
