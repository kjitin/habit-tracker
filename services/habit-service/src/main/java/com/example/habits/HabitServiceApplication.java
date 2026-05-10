package com.example.habits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.habits",
        "com.example.common.web",
        "com.example.common.security"
})
public class HabitServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HabitServiceApplication.class, args);
    }
}
