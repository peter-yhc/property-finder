package org.pyhc.propertyfinder.scraper.realestate.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateLink extends Query {

    private static final String REALESTATE_URL = "http://www.realestate.com.au";

    private String propertyLink;

    @Override
    public String toString() {
        return REALESTATE_URL + propertyLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealEstateLink that = (RealEstateLink) o;
        return Objects.equals(propertyLink, that.propertyLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyLink);
    }
}
