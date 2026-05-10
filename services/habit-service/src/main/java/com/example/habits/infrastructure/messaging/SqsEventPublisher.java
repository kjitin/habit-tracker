package com.example.habits.infrastructure.messaging;

import com.example.common.web.events.EventPublisher;
import io.awspring.cloud.sqs.operations.SqsTemplate;

import java.util.Map;

/**
 * Sends events to an SQS queue as JSON, with an "eventType" header
 * naming the event class so consumers can route or filter.
 */
public class SqsEventPublisher implements EventPublisher {

    private final SqsTemplate sqsTemplate;
    private final String queueName;

    public SqsEventPublisher(SqsTemplate sqsTemplate, String queueName) {
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
    }

    @Override
    public void publish(Object event) {
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(event)
                .headers(Map.of("eventType", event.getClass().getSimpleName())));
    }
}
