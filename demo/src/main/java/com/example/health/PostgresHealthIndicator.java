package com.example.health;



import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostgresHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public PostgresHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            // simple query to check DB connection
            jdbcTemplate.execute("SELECT 1");

            return Health.up()
                    .withDetail("PostgreSQL", "Available")
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("PostgreSQL", "Not reachable")
                    .withException(e)
                    .build();
        }
    }
}
