package org.pyhc.propertyfinder.scraper.realestate.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateLink extends Query {

    private static final String REALESTATE_URL = "http://www.realestate.com.au";

    private String propertyLink;

    @Override
    public String toString() {
        return REALESTATE_URL + propertyLink;
    }

}
