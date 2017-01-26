package org.pyhc.realestate.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(
        value = "org.pyhc.realestate",
        excludeFilters = @ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION)
)
@Import(value = {})
@EnableJpaRepositories
public class MainConfiguration {
}
