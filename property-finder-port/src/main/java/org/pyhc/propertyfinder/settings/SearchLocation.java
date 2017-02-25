package org.pyhc.propertyfinder.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchLocation {

    private String suburb;
    private String state;
    private Integer postcode;

    @Override
    public String toString() {
        return String.format("%s %s, %s", suburb, state, postcode);
    }
}
