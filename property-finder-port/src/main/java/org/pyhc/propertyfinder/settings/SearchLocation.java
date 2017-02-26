package org.pyhc.propertyfinder.settings;

import com.sun.istack.internal.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchLocation {

    @NotNull
    private String suburb;

    @NotNull
    private String state;

    @NotNull
    private Integer postcode;

    private SearchLocation() {
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s", suburb, state, postcode);
    }
}
