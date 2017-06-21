package org.pyhc.propertyfinder.scraper;


import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterConfiguration;
import org.pyhc.propertyfinder.property.PropertyProcessorPort;
import org.pyhc.propertyfinder.scraper.publisher.ScraperResultPublisher;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AdapterConfiguration.class, WebScraper_Base_Test.ContextConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class WebScraper_Base_Test {

    protected static final String REALESTATE_DOMAIN = "http://www.realestate.com.au";

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected WebScraper webScraper;

    protected MockRestServiceServer mockServer;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @After
    public void resetMocks() {
        mockServer.reset();
    }

    protected String loadPageFromTestResources(String resourcePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(resourcePath)).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public WebScraper webScraper(CompletableRestTemplate completableRestTemplate, ScraperResultPublisher scraperResultPublisher) {
            return new WebScraper(completableRestTemplate, scraperResultPublisher);
        }

        @Bean
        public CompletableRestTemplate completableRestTemplate(RestTemplate restTemplate) {
            return new CompletableRestTemplate(restTemplate);
        }


        @Bean
        public ScraperResultPublisher scraperResultPublisher() {
            return new ScraperResultPublisher();
        }

        @Bean
        public SearchLocationPort searchLocationPort() {
            return mock(SearchLocationPort.class);
        }

        @Bean
        public PropertyProcessorPort propertyProcessorPort() {
            return mock(PropertyProcessorPort.class);
        }
    }
}