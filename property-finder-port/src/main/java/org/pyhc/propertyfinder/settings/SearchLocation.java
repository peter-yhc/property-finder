package org.pyhc.propertyfinder.settings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchLocation {

    @NotNull
    private String suburbName;

    @NotNull
    private String state;

    @NotNull
    private Integer postcode;

    private SearchLocation() {
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s", suburbName, state, postcode);
    }
}
