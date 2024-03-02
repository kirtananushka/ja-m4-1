package com.tananushka.module04;

import com.tananushka.module04.entity.UserEntity;
import com.tananushka.module04.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner printUserEntities(UserRepository repository) {
        return args -> {
            List<UserEntity> users = repository.findAll();
            log.info("UserEntity records:");
            log.info("--------------------");
            log.info(users.toString());
            log.info("");
        };
    }
}
