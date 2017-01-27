package org.pyhc.propertyfinder.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.scraper.model.PropertyResult;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.pyhc.propertyfinder.scraper.model.RealEstateLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class HttpWebScraper implements WebScraper {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PropertyResult> query(Query query) {
        String response = restTemplate.getForObject(URI.create(query.toString()), String.class);
        Document page = Jsoup.parse(response);
        Element searchResultsTbl = page.getElementById("searchResultsTbl");
        Elements articles = searchResultsTbl.getElementsByTag("article");
        return articles.stream().map(article -> {
            String propertyLink = article.getElementsByTag("a").get(0).attributes().get("href");
            return queryDetailedPage(propertyLink);
        }).collect(Collectors.toList());
    }

    private PropertyResult queryDetailedPage(String propertyLink) {
        RealEstateLink link = RealEstateLink.builder().withLink(propertyLink).build();
        restTemplate.getForObject(URI.create(link.toString()), String.class);

        return PropertyResult.builder().propertyLink(propertyLink).build();
    }
}