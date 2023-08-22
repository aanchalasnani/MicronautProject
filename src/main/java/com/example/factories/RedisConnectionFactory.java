package com.example.factories;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.inject.Singleton;

@Singleton
public class RedisConnectionFactory {

    private final StatefulRedisConnection<String, String> connection;

    public RedisConnectionFactory() {
        RedisClient redisClient = RedisClient.create("redis://localhost");
        connection = redisClient.connect();
    }

    public StatefulRedisConnection<String, String> getConnection() {
        return connection;
    }

    public void closeConnection() {
        connection.close();
    }
}