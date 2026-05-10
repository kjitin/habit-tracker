package com.example.common.web.events;

/**
 * Port for emitting domain events. The implementation chosen at runtime
 * depends on profile: in-process for dev/test, SQS for production.
 */
public interface EventPublisher {
    void publish(Object event);
}
