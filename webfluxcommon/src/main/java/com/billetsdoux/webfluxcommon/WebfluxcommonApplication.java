package com.billetsdoux.webfluxcommon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories // 开启MongoDB的spring-data-jpa
@SpringBootApplication
public class WebfluxcommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxcommonApplication.class, args);
    }

}
