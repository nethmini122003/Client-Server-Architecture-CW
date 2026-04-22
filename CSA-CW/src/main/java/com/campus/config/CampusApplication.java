package com.campus.config;

import com.campus.filter.ApiLoggingFilter;
import com.campus.mapper.GlobalExceptionMapper;
import com.campus.mapper.LinkedResourceNotFoundExceptionMapper;
import com.campus.mapper.RoomNotEmptyExceptionMapper;
import com.campus.mapper.SensorUnavailableExceptionMapper;
import com.campus.resource.DiscoveryResource;
import com.campus.resource.RoomResource;
import com.campus.resource.SensorResource;
import org.glassfish.jersey.server.ResourceConfig;

public class CampusApplication extends ResourceConfig {
    public CampusApplication() {
        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);

        register(RoomNotEmptyExceptionMapper.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
        register(SensorUnavailableExceptionMapper.class);
        register(GlobalExceptionMapper.class);

        register(ApiLoggingFilter.class);
    }
}