package com.example.service.clients;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;

@Singleton
public class RedisCacheClient {

    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> redisCommands;

    public RedisCacheClient() {
        RedisClient redisClient = RedisClient.create("redis://localhost");
        connection = redisClient.connect();
        redisCommands = connection.sync();
    }

    public void set(String key, String value) {
        redisCommands.set(key, value);
    }

    public String get(String key) {
        return redisCommands.get(key);
    }

    public void setWithExpiration(String key, String value, long seconds) {
        redisCommands.setex(key, seconds, value);
    }

    public boolean exists(String key) {
        return redisCommands.exists(key) > 0;
    }

    public void delete(String key) {
        redisCommands.del(key);
    }

    public long increment(String key) {
        return redisCommands.incr(key);
    }

    public long decrement(String key) {
        return redisCommands.decr(key);
    }

    public void setHashValue(String hashKey, String field, String value) {
        redisCommands.hset(hashKey, field, value);
    }

    public String getHashValue(String hashKey, String field) {
        return redisCommands.hget(hashKey, field);
    }

    @PreDestroy
    public void closeConnection() {
        connection.close();
    }
}