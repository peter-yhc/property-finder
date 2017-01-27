package org.pyhc.propertyfinder.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.scraper.model.PropertyResult;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;


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
        articles.forEach(article -> {
            String detailedArticleLink = article.getElementsByTag("a").get(0).attributes().get("href");
            new PropertyResult(detailedArticleLink, null, null, null, null, null, null);
        });
        return null;
    }
}
/*
for article in soup.find(id='searchResultsTbl').find_all('article'):
        propertyLink = article.find('a')['href']
        if propertyLink in result:
            continue
        priceRange = str(minPrice) + '-' + str(maxPrice)
        parsePropertyPage(propertyLink, priceRange)
 */