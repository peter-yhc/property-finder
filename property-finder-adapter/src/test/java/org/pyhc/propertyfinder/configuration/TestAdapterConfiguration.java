package org.pyhc.propertyfinder.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TestAdapterConfiguration extends AdapterConfiguration{

    public TestAdapterConfiguration() {
        System.out.println("Build");
    }
}
