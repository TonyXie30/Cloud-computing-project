package com.whitepoor.eventviewer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.place}")
    private String placeSQL;

    @Value("${sql.event}")
    private String eventSQL;

    public void placeInitialization() {
        jdbcTemplate.execute(placeSQL);
        System.out.println("place initialization success");
    }

    public void eventInitialization() {
        jdbcTemplate.execute(eventSQL);
        System.out.println("event initialization success");
    }

    @Override
    public void run(ApplicationArguments args) {
        placeInitialization();
        eventInitialization();
    }
}
