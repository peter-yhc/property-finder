package org.pyhc.realestate.scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebScraperImpl implements WebScraper {


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void query(Query query) {
        String response = restTemplate.getForObject(query.toString(), String.class);
    }
}
