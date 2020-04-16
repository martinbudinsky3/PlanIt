package com.example.vavaplanit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VavaplanitApplication {

    public static void main(String[] args) {
        SpringApplication.run(VavaplanitApplication.class, args);
    }

    // po spusteni napisat do prehliadaca: http://localhost:8080/hello
    @RequestMapping("/hello")
    public String sayHello()
    {
        return "Hello Spring Boot aplication.";
    }

}
