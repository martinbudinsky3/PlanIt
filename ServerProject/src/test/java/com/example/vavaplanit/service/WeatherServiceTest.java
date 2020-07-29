package com.example.vavaplanit.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class WeatherServiceTest {
//    @Autowired
//    WeatherService weatherService;
//
//    @Test
//    public void latitudeIs39_03AndLongitudeIsMinus77_5() throws JsonProcessingException {
//        GeoLocation geoCoordinates = weatherService.getLocation("8.8.8.8");
//        assertEquals(39.03, geoCoordinates.getLatitude());
//        assertEquals(-77.5, geoCoordinates.getLongitude());
//    }
//
//    @Test
//    public void dailyWeatherTest() throws JsonProcessingException {
//        GeoLocation geoCoordinates = new GeoLocation(40.12, -96.66);
//        assertNull(weatherService.getWeather(geoCoordinates));
//    }
}