package com.example.habits.infrastructure.messaging;

import com.example.common.web.events.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Delegates to Spring's in-VM application events. Used in dev-local and tests
 * so we don't need real SQS to develop or run unit tests.
 */
public class InProcessEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher delegate;

    public InProcessEventPublisher(ApplicationEventPublisher delegate) {
        this.delegate = delegate;
    }

    @Override
    public void publish(Object event) {
        delegate.publishEvent(event);
    }
}
