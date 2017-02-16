package org.pyhc.propertyfinder.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("org.pyhc.propertyfinder")
@Import(value = {
        DatabaseConfiguration.class
})
public class CoreConfiguration {

}
