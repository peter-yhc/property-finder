package org.pyhc.propertyfinder.scraper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchOptions {

    private String suburb;
    private Integer postalCode;
    private Integer minBeds;
    private Integer maxBeds;
    private Integer bathrooms;
    private Integer carSpaces;
    private Integer minPrice;
    private Integer maxPrice;

}
