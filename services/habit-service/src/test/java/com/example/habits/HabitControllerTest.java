package com.example.habits;

import com.example.common.security.principal.AuthenticatedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev-local")
class HabitControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void createAndListHabits() throws Exception {
        UUID userId = UUID.randomUUID();
        var principal = new AuthenticatedUser(userId, "test@example.com");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, List.of()));

        try {
            mockMvc.perform(post("/api/v1/habits")
                    .contentType("application/json")
                    .content("""
                        {"name":"Drink water","frequency":"DAILY"}
                        """))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Drink water"));

            mockMvc.perform(get("/api/v1/habits"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Drink water"));
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
