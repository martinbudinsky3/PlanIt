package com.example.client;

import com.example.model.weather.DailyWeather;
import com.example.utils.PropertiesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherClient {

    private final PropertiesReader uriPropertiesReader = new PropertiesReader("uri.properties");
    private final String BASE_URI = uriPropertiesReader.getProperty("base-uri");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);

    private String getPublicIPadress() throws JsonProcessingException {
        logger.info("Getting public IP adress of device");
        final String uri = uriPropertiesReader.getProperty("get-public-ip-uri");

        String publicIPjson = restTemplate.getForObject(uri, String.class);
        Map<String, String> publicIPmap = objectMapper.readValue(publicIPjson, new TypeReference<Map<String, String>>() {});
        String publicIP = publicIPmap.get("ip");

        logger.debug("Public IP adress of client device: " + publicIP);

        return publicIP;
    }

    public List<DailyWeather> getWeather() {
        logger.info("Getting weather forecast");
        final String uri = BASE_URI + "/weather";

        List<DailyWeather> weatherForecast = new ArrayList<>();
        try {
            String publicIP = getPublicIPadress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("ip", publicIP);
            String weatherForecastJson = restTemplate.getForObject(uri, String.class, params);

            objectMapper.registerModule(new JavaTimeModule());
            weatherForecast = objectMapper.readValue(weatherForecastJson, new TypeReference<List<DailyWeather>>() {
            });
        } catch (JsonProcessingException ex) {
            logger.error("Error. Something went wrong with json processing.", ex);
        }

        return weatherForecast;
    }
}
