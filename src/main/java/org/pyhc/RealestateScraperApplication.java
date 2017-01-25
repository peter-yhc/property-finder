package org.pyhc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RealestateScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealestateScraperApplication.class, args);
    }
}
