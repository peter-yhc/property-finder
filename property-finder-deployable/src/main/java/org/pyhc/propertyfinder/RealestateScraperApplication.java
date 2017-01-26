package org.pyhc.propertyfinder;

import org.pyhc.propertyfinder.configuration.MainConfiguration;
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
