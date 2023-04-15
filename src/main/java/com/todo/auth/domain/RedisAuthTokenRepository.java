package com.todo.auth.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisAuthTokenRepository {

    private static final String KEY_PREFIX = "simple_todo_list:";
    private static final Duration BASIC_DURATION = Duration.ofDays(1);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> Optional<T> get(final String key, final Class<T> type) {
        log.info("[GET-redis] key : {}, type : {}", key, type);
        String serializedValue = redisTemplate.opsForValue().get(redisKey(key));
        try {
            return Optional.of(objectMapper.readValue(serializedValue, type));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void set(final String key, final Object value) {
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(redisKey(key), serializedValue, BASIC_DURATION);
            log.info("[SET-redis] key : {}, value : {}", key, serializedValue);
        } catch (Exception ignored) {}
    }

    public boolean delete(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(redisKey(key)));
    }

    private String redisKey(final String key) {
        return KEY_PREFIX + key;
    }
}
