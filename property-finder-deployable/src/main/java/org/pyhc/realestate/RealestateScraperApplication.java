package org.pyhc.realestate;

import org.pyhc.realestate.configuration.MainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MainConfiguration.class)
public class RealestateScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealestateScraperApplication.class, args);
    }
}
