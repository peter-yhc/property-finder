package org.pyhc.propertyfinder.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(
        value = "org.pyhc.propertyfinder",
        excludeFilters = @ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION))
@EnableWebMvc
public class AdapterConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
