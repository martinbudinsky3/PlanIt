package com.example.vavaplanit.service;

import com.example.vavaplanit.dto.mappers.WeatherDTOmapper;
import com.example.vavaplanit.model.GeoLocation;
import com.example.vavaplanit.model.weather.DailyWeather;
import com.example.vavaplanit.model.weather.WeatherForecast;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:uri.properties")
public class WeatherService {
    @Value("${weather-api-key}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherDTOmapper dailyWeatherDTOmapper;

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

    public List<DailyWeather> getWeather(GeoLocation geoLocation) throws JsonProcessingException{
        final String uri = "https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude=current,minutely,hourly" +
                "&appid={api-key}&units=metric";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lat", geoLocation.getLatitude());
        params.put("lon", geoLocation.getLongitude());
        params.put("api-key", API_KEY);

        String weatherJson = restTemplate.getForObject(uri, String.class, params);
        WeatherForecast weatherForecast = objectMapper.readValue(weatherJson, new TypeReference<WeatherForecast>() {
        });

        weatherForecast.getDaily().
                forEach(dailyWeather -> dailyWeather.getWeather().
                    forEach(weather -> weather.setIconUri(buildIconUri(weather.getIcon()))));

        return weatherForecast.getDaily();
    }

    private String buildIconUri(String iconName) {
        return MessageFormat.format("http://openweathermap.org/img/wn/{0}@2x.png", iconName);
    }
}
