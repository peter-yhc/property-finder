package org.pyhc.propertyfinder.scraper.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static java.lang.String.format;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateQuery extends Query {

    private static final String REALESTATE_URL = "http://www.realestate.com.au";

    private String suburb;
    private Integer postalCode;
    private Integer minBeds;
    private Integer maxBeds;
    private Integer bathrooms;
    private Integer carSpaces;
    private Integer minPrice;
    private Integer maxPrice;

    @Override
    public String toString() {
        suburb = suburb.replace(" ", "+");
        String initialBedroomQuery = "";
        if (minBeds != null || maxBeds != null) {
            initialBedroomQuery = format("with-%s-", minBeds == null ? "studio" : minBeds + "-bedrooms");
        }
        String priceQuery = "";
        if (minPrice != null || maxPrice != null) {
            priceQuery = format("between-%s-%s-", minPrice == null ? 0 : minPrice, maxPrice == null ? "any" : maxPrice);
        }
        return REALESTATE_URL + format("/buy/%s%sin-%s%%2c+nsw+%s/list-1?numBaths=%s&numParkingSpaces=%s&maxBeds=%s",
                initialBedroomQuery,
                priceQuery,
                suburb,
                postalCode,
                bathrooms == null ? "any" : bathrooms,
                carSpaces == null ? "any" : carSpaces,
                maxBeds == null ? "any" : maxBeds);
    }

}
