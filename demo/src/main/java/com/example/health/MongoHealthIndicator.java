package com.example.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoHealthIndicator implements HealthIndicator {


    private final MongoTemplate mongoTemplate;

    public MongoHealthIndicator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Health health() {
        try {
            // ping MongoDB
            mongoTemplate.executeCommand("{ ping: 1 }");

            return Health.up()
                    .withDetail("MongoDB", "Available")
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("MongoDB", "Not reachable")
                    .withException(e)
                    .build();
        }
    }
}
