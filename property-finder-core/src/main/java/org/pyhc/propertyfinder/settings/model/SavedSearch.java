package org.pyhc.propertyfinder.settings.model;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class SavedSearch {

    @Getter(AccessLevel.NONE)
    private String _id;
    private String name;
    private String state;
    private Integer postcode;

    @Builder
    private SavedSearch(String name, String state, Integer postcode) {
        this.name = name;
        this.state = state;
        this.postcode = postcode;
    }

    private SavedSearch() {
    }
}
