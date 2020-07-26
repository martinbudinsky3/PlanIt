package com.example.vavaplanit.Service;

import com.example.vavaplanit.Api.WeatherController;
import com.example.vavaplanit.Model.GeoLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

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
}
