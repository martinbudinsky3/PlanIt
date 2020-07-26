package com.example.vavaplanit.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @RequestMapping(value = "{ip}", method = RequestMethod.GET)
    public void getWeather(@PathVariable String ip) {

    }
}
