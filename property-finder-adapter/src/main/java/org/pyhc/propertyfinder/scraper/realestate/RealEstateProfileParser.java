package org.pyhc.propertyfinder.scraper.realestate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.pyhc.propertyfinder.scraper.model.PropertyProfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class RealEstateProfileParser {

    public static PropertyProfile parse(Document document, String propertyLink) {
        String priceEstimate = parsePriceEstimate(document.getElementById("listing_info").getElementsByTag("p").get(0).text());
        String address = document.getElementById("listing_header").getElementsByAttributeValue("itemprop", "streetAddress").text();
        String locality = document.getElementById("listing_header").getElementsByAttributeValue("itemprop", "addressLocality").text();
        String postalCode = document.getElementById("listing_header").getElementsByAttributeValue("itemprop", "postalCode").text();

        Element propertyInfo = document.getElementById("listing_info");

        String bed = getPropertyInfoElement(propertyInfo, "rui-icon-bed");
        String bath = getPropertyInfoElement(propertyInfo, "rui-icon-bath");
        String car = getPropertyInfoElement(propertyInfo, "rui-icon-car");

        String[] propertyLinkSplit = propertyLink.split("-");
        String propertyCode = propertyLinkSplit[propertyLinkSplit.length - 1];

        return PropertyProfile.builder()
                .propertyLink(propertyLink)
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

    private static String getPropertyInfoElement(Element propertyInfo, String elementName) {
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

    private static String parsePriceEstimate(String rawPriceEstimate) {
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
