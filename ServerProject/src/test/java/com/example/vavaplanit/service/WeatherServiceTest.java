package com.example.vavaplanit.service;

import com.example.vavaplanit.model.GeoLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherServiceTest {
    @Autowired
    WeatherService weatherService;

    @Test
    public void latitudeIs39_03AndLongitudeIsMinus77_5() throws JsonProcessingException {
        GeoLocation geoCoordinates = weatherService.getLocation("8.8.8.8");
        assertEquals(39.03, geoCoordinates.getLatitude());
        assertEquals(-77.5, geoCoordinates.getLongitude());
    }
}