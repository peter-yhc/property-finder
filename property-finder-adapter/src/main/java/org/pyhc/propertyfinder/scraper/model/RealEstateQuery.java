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
    private Integer carSpaces;

    @Override
    public String toString() {
        suburb = suburb.replace(" ", "+");
        String initialBedroomQuery = "";
        if (minBeds != null) {
            initialBedroomQuery = format("/with-%s-bedrooms-", minBeds);
        }
        return REALESTATE_URL + format("/buy%sin-%s%%2c+nsw+%s/list-1?numParkingSpaces=%s&maxBeds=%s",
                initialBedroomQuery,
                suburb,
                postalCode,
                carSpaces == null ? "any" : carSpaces,
                maxBeds == null ? "any" : maxBeds);
    }

}
