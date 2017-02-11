package org.pyhc.propertyfinder.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.exception.NextPageLinkNotFoundException;
import org.pyhc.propertyfinder.scraper.model.PropertyProfile;
import org.pyhc.propertyfinder.scraper.model.Query;
import org.pyhc.propertyfinder.scraper.model.RealEstateLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;


@Service
public class HttpWebScraper implements WebScraper {

    private boolean continueToNextPage = false;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PropertyProfile> query(Query query) {
        String rawPageHtml = restTemplate.getForObject(URI.create(query.toString()), String.class);
        Document page = Jsoup.parse(rawPageHtml);
        Element searchResultsTbl = page.getElementById("searchResultsTbl");
        Elements propertyResults = searchResultsTbl.getElementsByTag("article");

        List<PropertyProfile> results = new ArrayList<>();
        results.addAll(parsePropertySearchResults(propertyResults));

        if (continueToNextPage) {
            try {
                String nextPageHref = getNextPageHref(page);
                results.addAll(query(RealEstateLink.builder().propertyLink(nextPageHref).build()));
            } catch (NextPageLinkNotFoundException e) {
                // do nothing for now
            }
        }

        return results;
    }

    private String getNextPageHref(Document page) {
        Element pagesList = page.getElementById("sortBy").siblingElements()
                .stream()
                .filter(e -> e.tagName().equals("ul"))
                .findFirst()
                .orElseThrow(NextPageLinkNotFoundException::new);

        Elements nextLink = pagesList.getElementsByClass("nextLink");
        if (nextLink.size() < 1) {
            throw new NextPageLinkNotFoundException();
        }
        return nextLink.get(0).getElementsByAttribute("href").get(0).attr("href");
    }

    private List<PropertyProfile> parsePropertySearchResults(Elements propertySearchResults) {
        return propertySearchResults.stream().map(searchResult -> {
            String propertyLink = searchResult.getElementsByTag("a").get(0).attributes().get("href");
            RealEstateLink realEstateLink = RealEstateLink.builder().propertyLink(propertyLink).build();
            return queryProfilePage(realEstateLink);
        }).collect(Collectors.toList());
    }

    @Override
    public PropertyProfile queryProfilePage(Query query) {
        System.out.println("profile page");
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

        String bed = getPropertyInfoElement(propertyInfo, "rui-icon-bed");
        String bath = getPropertyInfoElement(propertyInfo, "rui-icon-bath");
        String car = getPropertyInfoElement(propertyInfo, "rui-icon-car");

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

    @Override
    public void recursivelySearchAllPages(boolean flag) {
        this.continueToNextPage = flag;
    }

    private String getPropertyInfoElement(Element propertyInfo, String elementName) {
        try {
            return propertyInfo.getElementsByTag("dt")
                    .stream()
                    .filter(dt -> dt.getElementsByClass(elementName).size() != 0)
                    .findFirst().orElseThrow(() -> new IllegalArgumentException(format("No matches in property info for %s.", elementName)))
                    .nextElementSibling()
                    .text();
        } catch (Exception e) {
            return "0";
        }
    }

    private String parsePriceEstimate(String rawPriceEstimate) {
        rawPriceEstimate = rawPriceEstimate.replace(",", "");
        Pattern pattern = Pattern.compile("[0-9]{5,7}|(?i)Auction|(?i)Contact");
        Matcher matcher = pattern.matcher(rawPriceEstimate);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group());
            builder.append("-");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}