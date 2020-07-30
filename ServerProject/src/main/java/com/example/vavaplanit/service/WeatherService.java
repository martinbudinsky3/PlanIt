package com.example.vavaplanit.service;

import com.example.vavaplanit.model.GeoLocation;
import com.example.vavaplanit.model.dto.DailyWeatherDTO;
import com.example.vavaplanit.model.weather.WeatherForecast;
import com.example.vavaplanit.service.mappers.DailyWeatherDTOmapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO uri's to config file

@Service
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DailyWeatherDTOmapper dailyWeatherDTOmapper;

    Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public GeoLocation getLocation(String ip) throws JsonProcessingException {
        String uri = "http://ip-api.com/json/{ip}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("ip", ip);

        String geoLocationJson = restTemplate.getForObject(uri, String.class, params);
        Map<String, Object> geoLocationMap = objectMapper.readValue(geoLocationJson, new TypeReference<Map<String, Object>>() {});
        if("fail".equals(geoLocationMap.get("status"))) {
            return null;
        }

        GeoLocation geoCoordinates = new GeoLocation((Double) geoLocationMap.get("lat"), (Double) geoLocationMap.get("lon"));
        logger.debug("Latitude: " + geoCoordinates.getLatitude() + " Longitude: " + geoCoordinates.getLongitude());

        return geoCoordinates;
    }

    public byte[] getImage(String icon) {
        String uri = "http://openweathermap.org/img/wn/{icon}@2x.png";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("icon", icon);

        return restTemplate.getForObject(uri, byte[].class, params);
    }

    public List<DailyWeatherDTO> getWeather(GeoLocation geoLocation) throws JsonProcessingException{
        // TODO constants to configuration file
        String uri = "https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude=current,minutely,hourly" +
                "&appid={api-key}&units={units}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", geoLocation.getLatitude());
        params.put("lon", geoLocation.getLongitude());
        params.put("api-key", "bf115f2ec4f3aaf15a3585f872e895de");
        params.put("units", "metric");

        String weatherJson = restTemplate.getForObject(uri, String.class, params);
        WeatherForecast weatherForecast = objectMapper.readValue(weatherJson, new TypeReference<WeatherForecast>() {
        });

        weatherForecast.getDaily().
                stream().
                forEach(dailyWeather -> dailyWeather.getWeather().
                    stream().
                    forEach(weather -> weather.setIconImage(getImage(weather.getIcon()))));

        return weatherForecast.getDaily().
                stream().
                map(dailyWeatherDTOmapper::dailyWeatherToDailyWeatherDTO).
                collect(Collectors.toList());
    }
}
