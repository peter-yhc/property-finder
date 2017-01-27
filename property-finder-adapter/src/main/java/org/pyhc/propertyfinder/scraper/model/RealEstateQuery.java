package org.pyhc.propertyfinder.scraper.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateQuery extends Query {

    private static final String REALESTATE_URL = "http://www.realestate.com.au";

    private String suburb;
    private Integer postalCode;

    @Override
    public String toString() {
        return REALESTATE_URL + String.format("/buy/in-%s%%2c+nsw+%s/list-1?source=location-search", suburb, postalCode);
    }

}
