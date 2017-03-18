package org.pyhc.propertyfinder.scraper.realestate.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pyhc.propertyfinder.scraper.SearchOptions;

import java.util.Objects;

import static java.lang.String.format;

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

    public static RealEstateQuery fromSearchOptions(SearchOptions searchOptions) {
        return new RealEstateQuery(
                searchOptions.getSuburb(),
                searchOptions.getPostcode(),
                searchOptions.getMinBeds(),
                searchOptions.getMaxBeds(),
                searchOptions.getBathrooms(),
                searchOptions.getCarSpaces(),
                searchOptions.getMinPrice(),
                searchOptions.getMaxPrice()
        );
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealEstateQuery that = (RealEstateQuery) o;
        return Objects.equals(suburb, that.suburb) &&
                Objects.equals(postalCode, that.postalCode) &&
                Objects.equals(minBeds, that.minBeds) &&
                Objects.equals(maxBeds, that.maxBeds) &&
                Objects.equals(bathrooms, that.bathrooms) &&
                Objects.equals(carSpaces, that.carSpaces) &&
                Objects.equals(minPrice, that.minPrice) &&
                Objects.equals(maxPrice, that.maxPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suburb, postalCode, minBeds, maxBeds, bathrooms, carSpaces, minPrice, maxPrice);
    }
}
