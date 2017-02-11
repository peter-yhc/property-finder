package org.pyhc.propertyfinder.scraper;


import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@AdapterTest
public abstract class HttpWebScraper_Base_Test {
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
    public void reset() {
        mockServer.reset();
    }

    protected String loadPageFromTestResources(String resourcePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(resourcePath)).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}