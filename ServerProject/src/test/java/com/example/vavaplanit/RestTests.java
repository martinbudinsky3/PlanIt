package com.example.vavaplanit;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


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
