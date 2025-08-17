package com.example.bookapi.application.out;

public interface MessagePublisher {
    void publish(String topic, Object message);
}
