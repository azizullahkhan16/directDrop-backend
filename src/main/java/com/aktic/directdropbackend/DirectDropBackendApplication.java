package com.aktic.directdropbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DirectDropBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectDropBackendApplication.class, args);
    }

}
