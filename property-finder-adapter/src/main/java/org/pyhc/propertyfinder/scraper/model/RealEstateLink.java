package org.pyhc.propertyfinder.scraper.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateLink extends Query {

    private static final String REALESTATE_URL = "http://www.realestate.com.au";

    private String propertyLink;

    @Override
    public String toString() {
        return REALESTATE_URL + propertyLink;
    }

    public static RealEstateLink.Builder builder() {
        return new RealEstateLink.Builder();
    }

    public static class Builder {

        private String propertyLink;

        public RealEstateLink.Builder withLink(String link) {
            this.propertyLink = link;
            return this;
        }

        public RealEstateLink build() {
            return new RealEstateLink(propertyLink);
        }

    }
}
