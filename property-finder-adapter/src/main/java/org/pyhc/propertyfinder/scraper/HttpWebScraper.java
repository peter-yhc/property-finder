package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.model.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Service
public class HttpWebScraper implements WebScraper {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void query(Query query) {
        String response = restTemplate.getForObject(URI.create(query.toString()), String.class);
        System.out.println(response);
    }
}
