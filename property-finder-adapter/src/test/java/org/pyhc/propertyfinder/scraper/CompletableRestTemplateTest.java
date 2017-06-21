package org.pyhc.propertyfinder.scraper;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompletableRestTemplateTest.ContextConfiguration.class)
public class CompletableRestTemplateTest {

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CompletableRestTemplate completableRestTemplate;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void canMakeExternalCallsUsingQueryObject_AndParseDocument() throws Exception{
        mockServer.expect(once(), requestTo("http://www.realestate.com.au/buy/in-jolimont,+wa+6014"))
                .andRespond(withSuccess("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Hello Title</title></head><body></body></html>", TEXT_HTML));

        Query query = RealEstateLink.builder().propertyLink("/buy/in-jolimont,+wa+6014").build();
        Document document = completableRestTemplate.performGet(query).get();

        assertThat(document.title(), is("Hello Title"));
        mockServer.verify();
    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public CompletableRestTemplate completableRestTemplate(RestTemplate restTemplate) {
            return new CompletableRestTemplate(restTemplate);
        }

    }
}