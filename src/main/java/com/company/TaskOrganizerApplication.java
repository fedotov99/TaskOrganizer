package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class TaskOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskOrganizerApplication.class, args);
    }
}
