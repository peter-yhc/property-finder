package org.pyhc.propertyfinder.suburb;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

public class TestSuburbDetailsBuilder {

    public static SuburbDetails randomSuburbDetails() {
        return SuburbDetails.builder()
                .postcode(RandomUtils.nextInt())
                .suburbName(RandomStringUtils.randomAlphabetic(10))
                .state(RandomStringUtils.randomAlphabetic(3))
                .uuid(UUID.randomUUID())
                .build();
    }
}
