package com.example.vavaplanit.api;

import com.example.vavaplanit.dto.mappers.WeatherDTOmapper;
import com.example.vavaplanit.dto.weather.DailyWeatherDTO;
import com.example.vavaplanit.model.GeoLocation;
import com.example.vavaplanit.model.weather.DailyWeather;
import com.example.vavaplanit.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private WeatherDTOmapper weatherDTOmapper;

    @GetMapping(value = "{ip}")
    public ResponseEntity getWeather(@PathVariable String ip) {
        logger.info("Getting weather forecast for location by public IP " + ip);
        try {
            GeoLocation geoLocation = weatherService.getLocation(ip);
            if(geoLocation == null) {
                return new ResponseEntity<>("Unable to get location by IP", HttpStatus.PRECONDITION_FAILED);
            }

            List<DailyWeather> weatherList = weatherService.getWeather(geoLocation);
            List<DailyWeatherDTO>weatherDTOlist = weatherDTOmapper.dailyWeatherToDailyWeatherDTO(weatherList);

            return new ResponseEntity<>(weatherDTOlist, HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            logger.error("Error while processing Json", ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
