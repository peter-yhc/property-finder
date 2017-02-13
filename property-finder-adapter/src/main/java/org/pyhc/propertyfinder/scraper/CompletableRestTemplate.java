package org.pyhc.propertyfinder.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Component
public class CompletableRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Document> performGet(Query query) {
        return CompletableFuture
                .supplyAsync(() -> restTemplate.getForObject(URI.create(query.toString()), String.class))
                .thenApply(Jsoup::parse);
    }
}
