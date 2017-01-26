package org.pyhc.realestate.scraper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RealEstateQuery extends Query {

    private RealEstateQuery(){}

    private String suburb;
    private Integer postalCode;

    @Override
    public String toString() {
        return String.format("in-%s%%2c+nsw+%s/list-1?source=location-search", suburb, postalCode);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String suburb;
        private Integer postalCode;

        public Builder withSuburb(String suburb, Integer postalCode) {
            this.suburb = suburb;
            this.postalCode = postalCode;
            return this;
        }

        public RealEstateQuery build() {

            return new RealEstateQuery(suburb, postalCode);

        }

    }
}
