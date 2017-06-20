package org.pyhc.propertyfinder.scraper.realestate.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pyhc.propertyfinder.settings.SearchLocation;

import static java.lang.String.format;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateSoldQuery extends Query {

    private static final String REALESTATE_URL = "https://www.realestate.com.au";

    private String suburb;
    private Integer postalCode;
    private Integer page;

    @Override
    public String toString() {
        suburb = suburb.replace(" ", "+");
        return REALESTATE_URL + format("/sold/in-%s%%2c+%d/list-%d?includeSurrounding=false&misc=ex-no-sale-price&activeSort=solddate",
                suburb,
                postalCode,
                page
        );
    }

    public static RealEstateSoldQuery fromSearchOptions(SearchLocation searchLocation) {
        return fromSearchOptions(searchLocation, 1);
    }

    public static RealEstateSoldQuery fromSearchOptions(SearchLocation searchLocation, Integer page) {
        return new RealEstateSoldQuery(
                searchLocation.getSuburbName(),
                searchLocation.getPostcode(),
                page
        );
    }
}
