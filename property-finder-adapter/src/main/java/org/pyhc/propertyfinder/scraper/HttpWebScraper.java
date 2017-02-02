package org.pyhc.propertyfinder.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.pyhc.propertyfinder.scraper.model.RealEstateLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


@Service
public class HttpWebScraper implements WebScraper {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PropertyProfile> query(Query query) {
        String rawPageHtml = restTemplate.getForObject(URI.create(query.toString()), String.class);
        Document page = Jsoup.parse(rawPageHtml);
        Element searchResultsTbl = page.getElementById("searchResultsTbl");
        Elements articles = searchResultsTbl.getElementsByTag("article");
        return articles.stream().map(article -> {
            String propertyLink = article.getElementsByTag("a").get(0).attributes().get("href");
            RealEstateLink realEstateLink = RealEstateLink.builder().propertyLink(propertyLink).build();
            return queryProfilePage(realEstateLink);
        }).collect(Collectors.toList());
    }

    @Override
    public PropertyProfile queryProfilePage(Query query) {
        String[] propertyLinkSplit = query.toString().split("-");
        String propertyCode = propertyLinkSplit[propertyLinkSplit.length - 1];

        String rawPageHtml = restTemplate.getForObject(URI.create(query.toString()), String.class);
        if (rawPageHtml == null) {
            return null;
        }
        Document page = Jsoup.parse(rawPageHtml);
        String priceEstimate = parsePriceEstimate(page.getElementById("listing_info").getElementsByTag("p").get(0).text());
        String address = page.getElementById("listing_header").getElementsByAttributeValue("itemprop", "streetAddress").text();
        String locality = page.getElementById("listing_header").getElementsByAttributeValue("itemprop", "addressLocality").text();
        String postalCode = page.getElementById("listing_header").getElementsByAttributeValue("itemprop", "postalCode").text();

        Element propertyInfo = page.getElementById("listing_info");

        String bed = propertyInfo.getElementsByTag("dt").get(0).getElementsByClass("rui-icon-bed").get(0).nextElementSibling().text();
        String bath = propertyInfo.getElementsByTag("dt").get(1).getElementsByClass("rui-icon-bath").get(0).nextElementSibling().text();
        String car = propertyInfo.getElementsByTag("dt").get(2).getElementsByClass("rui-icon-car").get(0).nextElementSibling().text();

        return PropertyProfile.builder()
                .propertyLink(query.toString())
                .propertyCode(propertyCode)
                .priceEstimate(priceEstimate)
                .bed(parseInt(bed))
                .bath(parseInt(bath))
                .car(parseInt(car))
                .address(address)
                .suburb(locality)
                .postalCode(parseInt(postalCode))
                .build();
    }

    private String parsePriceEstimate(String rawPriceEstimate) {
        rawPriceEstimate = rawPriceEstimate.replace(",", "");
        Pattern pattern = Pattern.compile("[0-9]{5,7}|(?i)Auction");
        Matcher matcher = pattern.matcher(rawPriceEstimate);
        StringBuilder builder = new StringBuilder();
        while(matcher.find()) {
            builder.append(matcher.group());
            builder.append("-");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}