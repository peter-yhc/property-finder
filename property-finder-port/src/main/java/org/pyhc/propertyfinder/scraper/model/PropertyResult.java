package org.pyhc.propertyfinder.scraper.model;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyResult {

    private String propertyLink;
    private String priceRange;
    private String address;
    private Integer bed;
    private Integer bath;
    private Integer car;
    private Integer price;
    private String suburb;
    private Integer postalCode;
    private String propertyCode;

}

