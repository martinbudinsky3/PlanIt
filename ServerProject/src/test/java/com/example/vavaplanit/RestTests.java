package com.example.vavaplanit;

import com.example.vavaplanit.Model.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
public class RestTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void eventTest() throws Exception{
//        // add
//        RestTemplate restTemplate = new RestTemplate();
//
//        LocalDate date = LocalDate.parse("2020-04-30");
//        LocalTime starts = LocalTime.parse("11:15");
//        LocalTime ends = LocalTime.parse("11:30");
//        LocalTime alert = LocalTime.parse("11:00");
//        Event event = new Event("title", "location", date, starts, ends, alert);
//        String uri = "http://localhost:8080/events";
//        String id = restTemplate.postForObject(uri, event, String.class);
//        Integer idEvent = objectMapper.readValue(id, Integer.class);
//
//        event.setIdEvent(idEvent);
//
//        // get
//        uri = "http://localhost:8080/events/{idUser}/{idEvent}";
//        Map<String, Integer> params = new HashMap<String, Integer>();
//        params.put("idUser", 1);
//        params.put("idEvent", idEvent);
//
//        restTemplate = new RestTemplate();
//        String eventJSon = restTemplate.getForObject(uri, String.class, params);
//        objectMapper.registerModule(new JavaTimeModule());
//        Event returnedEvent = objectMapper.readValue(eventJSon, new TypeReference<Event>(){});
//        Assert.assertEquals(event, returnedEvent);
//    }
}
