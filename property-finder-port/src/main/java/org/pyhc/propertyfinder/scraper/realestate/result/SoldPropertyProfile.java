package org.pyhc.propertyfinder.scraper.realestate.result;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SoldPropertyProfile {

    private String address;
    private Integer bed;
    private Integer bath;
    private Integer car;
    private Integer price;
    private String suburb;
    private Integer postcode;
    private LocalDate soldDate;
    private String propertyLink;
    private Integer propertyCode;

    private SoldPropertyProfile() {
    }

}

