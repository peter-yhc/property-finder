package org.pyhc.propertyfinder.scraper.realestate.query;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyProfile {

    private String propertyLink;
    private String priceEstimate;
    private String address;
    private Integer bed;
    private Integer bath;
    private Integer car;
    private Integer price;
    private String suburb;
    private Integer postalCode;
    private String propertyCode;

}

