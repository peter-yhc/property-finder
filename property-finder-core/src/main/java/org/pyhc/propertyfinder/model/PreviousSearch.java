package org.pyhc.propertyfinder.model;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PreviousSearch {

    @Getter(AccessLevel.NONE)
    private String _id;
    private String name;
    private String state;
    private Integer postcode;
    private UUID uuid;

    @Builder
    private PreviousSearch(String name, String state, Integer postcode) {
        this.name = name;
        this.state = state;
        this.postcode = postcode;
        this.uuid = UUID.randomUUID();
    }

    private PreviousSearch() {
    }
}
