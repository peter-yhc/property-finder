package org.pyhc.propertyfinder;

import org.pyhc.propertyfinder.configuration.CoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class RealestateScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealestateScraperApplication.class, args);
    }
}
