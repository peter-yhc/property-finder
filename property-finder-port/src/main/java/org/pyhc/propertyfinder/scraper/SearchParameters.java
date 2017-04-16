package org.pyhc.propertyfinder.scraper;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class SearchParameters {

    private String suburb;
    private Integer postcode;
    private Integer minBeds;
    private Integer maxBeds;
    private Integer bathrooms;
    private Integer carSpaces;
    private Integer minPrice;
    private Integer maxPrice;

}
