package org.pyhc.propertyfinder.scraper.realestate;

import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.pyhc.propertyfinder.suburb.SuburbDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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

    public static List<SoldPropertyProfile> parseSoldProperties(Document document, SuburbDetails suburbDetails) {
        return document.getElementsByTag("article")
                .stream()
                .map(articleElement -> parseResultSummary(articleElement, suburbDetails))
                .collect(toList());
    }

    private static SoldPropertyProfile parseResultSummary(Element summary, SuburbDetails suburbDetails) {
        String propertyLink = "https://www.realestate.com.au" + summary.getElementsByClass("property-card__link").attr("href");

        Integer beds;
        Integer baths;
        Integer cars;
        if (propertyLink.contains("property-residential+land")) {
            beds = 0;
            baths = 0;
            cars = 0;
        } else {
            beds = parseConfiguration(summary, "general-features__beds");
            baths = parseConfiguration(summary, "general-features__baths");
            cars = parseConfiguration(summary, "general-features__cars");
        }

        Integer price = parsePrice(summary.getElementsByClass("property-price").get(0));
        String address = parseAddress(summary.getElementsByClass("property-card__info-text").get(0).text());

        LocalDate soldDate = parseSoldDate(summary.getElementsByClass("property-card__with-comma").get(0).text());

        Integer propertyCode = parsePropertyCode(propertyLink);

        return SoldPropertyProfile
                .builder()
                .price(price)
                .address(address)
                .suburb(WordUtils.capitalize(suburbDetails.getSuburbName()))
                .postcode(suburbDetails.getPostcode())
                .soldDate(soldDate)
                .bed(beds)
                .bath(baths)
                .car(cars)
                .propertyLink(propertyLink)
                .propertyCode(propertyCode)
                .build();
    }

    private static Integer parseConfiguration(Element summary, String className) {
        try {
            return parseInt(summary.getElementsByClass(className).get(0).text());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
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

    private static Integer parsePrice(Element priceElement) {
        String priceText = priceElement.text();
        if (priceText == null || priceElement.text().equals("")) {
            priceText = parsePriceImage(priceElement);
        }

        try {
            priceText = priceText.replaceAll(",", "");
            priceText = priceText.replaceAll("\\$", "");
        } catch (Exception e) {
            System.out.println(priceElement.toString());
            e.printStackTrace();
        }
        return parseInt(priceText);
    }

    private static String parsePriceImage(Element priceElement) {
        String priceImageLink = priceElement.getElementsByClass("property-price__image").attr("src");
        Matcher matcher = Pattern.compile("convert\\/([A-Za-z0-9]*)=").matcher(priceImageLink);
        String priceText = null;
        if (matcher.find()) {
            priceText = matcher.group(1);
            priceText = new String(Base64.getDecoder().decode(priceText));
        }
        return priceText;
    }
}
