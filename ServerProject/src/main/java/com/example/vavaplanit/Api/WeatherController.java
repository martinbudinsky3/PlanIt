package com.example.vavaplanit.Api;

import com.example.vavaplanit.Model.GeoLocation;
import com.example.vavaplanit.Service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    @RequestMapping(value = "{ip}", method = RequestMethod.GET)
    public ResponseEntity getWeather(@PathVariable String ip) {
        try {
            GeoLocation geoLocation = weatherService.getLocation(ip);
            if(geoLocation == null) {
                return new ResponseEntity<>("Unable to get location by IP", HttpStatus.PRECONDITION_FAILED);
            }


        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return null;
    }
}
