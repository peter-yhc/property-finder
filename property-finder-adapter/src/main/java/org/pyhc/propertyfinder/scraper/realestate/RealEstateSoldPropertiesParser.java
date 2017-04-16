package org.pyhc.propertyfinder.scraper.realestate;

import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.scraper.SearchParameters;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

public class RealEstateSoldPropertiesParser {

    private static final Pattern PATTERN_FOR_COUNTS = Pattern.compile("of ([0-9]+) results");
    private static final Pattern PATTERN_FOR_PROPERTY_CODE = Pattern.compile("-([0-9]+$)");
    private static final Pattern PATTERN_FOR_SOLD_DATE = Pattern.compile("(Sold on )(.+)");
    private static final DateTimeFormatter PATTERN_FOR_SOLD_DATE_PARSER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public static Integer getSoldPropertiesCount(Document document) {
        Elements matchedClasses = document.getElementsByClass("total-results-count");
        if (matchedClasses.size() > 0) {
            Element totalResultsCount = matchedClasses.get(0);
            Matcher matcher = PATTERN_FOR_COUNTS.matcher(totalResultsCount.text());
            if (matcher.find()) {
                return parseInt(matcher.group(1));
            }
        }
        return 0;
    }

    public static List<SoldPropertyProfile> parseSoldProperties(Document document, SearchParameters searchParameters) {
        return document.getElementsByTag("article")
                .stream()
                .map(articleElement -> parseResultSummary(articleElement, searchParameters))
                .collect(toList());
    }

    private static SoldPropertyProfile parseResultSummary(Element summary, SearchParameters searchParameters) {
        Integer price = parsePrice(summary.getElementsByClass("property-price").get(0).text());
        String address = parseAddress(summary.getElementsByClass("property-card__info-text").get(0).text());
        Integer beds = parseInt(summary.getElementsByClass("general-features__beds").get(0).text());
        Integer baths = parseInt(summary.getElementsByClass("general-features__baths").get(0).text());
        Integer cars = parseInt(summary.getElementsByClass("general-features__cars").get(0).text());
        LocalDate soldDate = parseSoldDate(summary.getElementsByClass("property-card__with-comma").get(0).text());

        String propertyLink = "https://www.realestate.com.au" + summary.getElementsByClass("property-card__link").attr("href");
        Integer propertyCode = parsePropertyCode(propertyLink);

        return SoldPropertyProfile
                .builder()
                .price(price)
                .address(address)
                .suburb(WordUtils.capitalize(searchParameters.getSuburb()))
                .postcode(searchParameters.getPostcode())
                .soldDate(soldDate)
                .bed(beds)
                .bath(baths)
                .car(cars)
                .propertyLink(propertyLink)
                .propertyCode(propertyCode)
                .build();
    }

    private static LocalDate parseSoldDate(String soldDate) {
        Matcher matcher = PATTERN_FOR_SOLD_DATE.matcher(soldDate);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(2), PATTERN_FOR_SOLD_DATE_PARSER);
        }
        return null;
    }

    private static Integer parsePropertyCode(String propertyLink) {
        Matcher matcher = PATTERN_FOR_PROPERTY_CODE.matcher(propertyLink);
        if (matcher.find()) {
            return parseInt(matcher.group(1));
        }
        return null;
    }

    private static String parseAddress(String address) {
        if (address.charAt(address.length() - 1) == ',') {
            address = address.substring(0, address.length() - 1);
        }
        return address;
    }

    private static Integer parsePrice(String priceText) {
        priceText = priceText.replaceAll(",", "");
        priceText = priceText.replaceAll("\\$", "");
        return parseInt(priceText);
    }
}
