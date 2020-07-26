package com.example.vavaplanit.Service;

import com.example.vavaplanit.Model.GeoLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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