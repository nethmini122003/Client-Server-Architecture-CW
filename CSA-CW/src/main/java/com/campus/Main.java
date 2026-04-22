package com.campus;

import com.campus.config.CampusApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) throws IOException {
        CampusApplication application = new CampusApplication();
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), application);

        System.out.println("Smart Campus API started at " + BASE_URI);
        System.out.println("Press Enter to stop the server.");
        System.in.read();

        server.shutdownNow();
    }
}
