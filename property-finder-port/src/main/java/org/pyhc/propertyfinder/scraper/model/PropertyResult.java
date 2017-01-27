package org.pyhc.propertyfinder.scraper.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyResult {

    private String href;
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

