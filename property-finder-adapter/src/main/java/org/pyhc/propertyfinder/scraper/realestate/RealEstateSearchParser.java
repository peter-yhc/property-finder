package org.pyhc.propertyfinder.scraper.realestate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.exception.NextPageLinkNotFoundException;
import org.pyhc.propertyfinder.scraper.realestate.query.RealEstateLink;
import org.pyhc.propertyfinder.scraper.realestate.result.SearchResult;

import java.util.List;
import java.util.stream.Collectors;

public class RealEstateSearchParser {

    public static SearchResult parse(Document document) {
        RealEstateLink nextPageLink = getNextPageLink(document);

        Element searchResultsTbl = document.getElementById("searchResultsTbl");
        Elements propertyResults = searchResultsTbl.getElementsByTag("article");
        List<RealEstateLink> realEstateLinks = parsePropertySearchResults(propertyResults);

        return SearchResult.builder().nextPageLink(nextPageLink).profileLinks(realEstateLinks).build();
    }

    private static RealEstateLink getNextPageLink(Document document) {
        String href;
        try {
            Element pagesList = document.getElementById("sortBy").siblingElements()
                    .stream()
                    .filter(e -> e.tagName().equals("ul"))
                    .findFirst()
                    .orElseThrow(NextPageLinkNotFoundException::new);
            Elements nextLink = pagesList.getElementsByClass("nextLink");
            if (nextLink.size() < 1) {
                throw new NextPageLinkNotFoundException();
            }
            href = nextLink.get(0).getElementsByAttribute("href").get(0).attr("href");
        } catch (Exception e) {
            return null;
        }
        return RealEstateLink.builder().propertyLink(href).build();
    }

    private static List<RealEstateLink> parsePropertySearchResults(Elements propertySearchResults) {
        return propertySearchResults.stream().map(searchResult -> {
            String propertyLink = searchResult.getElementsByTag("a").get(0).attributes().get("href");
            return RealEstateLink.builder().propertyLink(propertyLink).build();
        }).collect(Collectors.toList());
    }
}
