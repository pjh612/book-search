package com.example.bookapi.application.out;

import java.time.Duration;
import java.util.Optional;

public interface CacheProvider {
    <T> Optional<T> get(String key, Class<T> clazz);

    boolean hasKey(String key);

    void save(String key, Object value, Duration duration);

    void delete(String key);
}
