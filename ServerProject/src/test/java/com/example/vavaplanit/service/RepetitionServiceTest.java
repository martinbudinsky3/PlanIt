package com.example.vavaplanit.service;

import com.example.vavaplanit.Main;
import com.example.vavaplanit.PostgresqlContainer;
import com.example.vavaplanit.model.repetition.Repetition;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepetitionServiceTest {
//    @ClassRule
//    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
//
//    @Autowired
//    private RepetitionService repetitionService;
//
//    @Test
//    public void insert() {
//        Repetition repetition = new Repetition(1L, LocalDate.of(2020, 8, 26),
//                LocalDate.of(2021,2,26), 1);
//
//        Long eventId = repetitionService.add(repetition);
//
//        assertEquals(1, eventId);
//    }
}