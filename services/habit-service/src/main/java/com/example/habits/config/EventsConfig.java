package com.example.habits.config;

import com.example.common.web.events.EventPublisher;
import com.example.habits.infrastructure.messaging.InProcessEventPublisher;
import com.example.habits.infrastructure.messaging.SqsEventPublisher;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventsConfig {

    @Bean
    @Profile("dev-local")
    public EventPublisher inProcessEventPublisher(ApplicationEventPublisher delegate) {
        return new InProcessEventPublisher(delegate);
    }

    @Bean
    @Profile("!dev-local")
    public EventPublisher sqsEventPublisher(
            SqsTemplate sqsTemplate,
            @Value("${app.events.queue-name}") String queueName) {
        return new SqsEventPublisher(sqsTemplate, queueName);
    }
}
