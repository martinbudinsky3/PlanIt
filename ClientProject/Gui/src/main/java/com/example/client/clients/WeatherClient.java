package com.example.client.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherClient {

    private final String BASE_URI = "http://localhost:8080";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);

    private String getPublicIPadress() throws JsonProcessingException {
        logger.info("Getting public IP adress of device");
        final String uri = "https://api.ipify.org?format=json";

        String publicIP = "";
        String publicIPjson = restTemplate.getForObject(uri, String.class);
        Map<String, String> publicIPmap = objectMapper.readValue(publicIPjson, new TypeReference<Map<String, String>>() {});
        publicIP = publicIPmap.get("ip");

        logger.debug("Public IP adress of client device: " + publicIP);
        return publicIP;
    }

    public void getWeather() {
        logger.info("Getting weather forecast");
        final String uri = BASE_URI + "/weather/{ip}";

        try {
            String publicIP = getPublicIPadress();
            Map<String, String> params = new HashMap<String, String>();
            params.put("ip", publicIP);
            String weatherForecastJson = restTemplate.getForObject(uri, String.class, params);
        } catch (JsonProcessingException ex) {
            logger.error("Error. Something went wrong with json processing.", ex);
        }
    }
}
