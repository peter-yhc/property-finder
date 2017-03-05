package org.pyhc.propertyfinder.property;

import org.apache.commons.lang3.RandomStringUtils;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;

public class TestPropertyProfile  {

    public static PropertyProfile randomProfile() {
        return PropertyProfile.builder()
                .address(RandomStringUtils.random(10))
                .bath(2)
                .bed(2)
                .car(2)
                .postalCode(2000)
                .price(300000)
                .priceEstimate("$300000-$350000")
                .propertyCode(RandomStringUtils.random(10))
                .propertyLink(RandomStringUtils.random(10))
                .build();
    }

}
