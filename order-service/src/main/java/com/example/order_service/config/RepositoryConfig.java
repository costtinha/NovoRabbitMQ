package com.example.order_service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.order_service.persistance")
@EnableRedisRepositories(basePackages = "com.example.order_service.cache")
@EntityScan(basePackages = "com.example.order_service.entity")
public class RepositoryConfig {
}
