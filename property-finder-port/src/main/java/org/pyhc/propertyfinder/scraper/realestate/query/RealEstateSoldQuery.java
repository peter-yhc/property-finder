package org.pyhc.propertyfinder.scraper.realestate.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pyhc.propertyfinder.scraper.SearchParameters;

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

    public static RealEstateSoldQuery fromSearchOptions(SearchParameters searchParameters) {
        return fromSearchOptions(searchParameters, 1);
    }

    public static RealEstateSoldQuery fromSearchOptions(SearchParameters searchParameters, Integer page) {
        return new RealEstateSoldQuery(
                searchParameters.getSuburb(),
                searchParameters.getPostcode(),
                page
        );
    }
}
