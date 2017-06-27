package org.pyhc.propertyfinder.model;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Suburb {

    @Getter(AccessLevel.NONE)
    private String _id;

    private String name;
    private String state;
    private Integer postcode;

    @Builder
    private Suburb(String name, String state, Integer postcode) {
        this.name = name;
        this.state = state;
        this.postcode = postcode;
    }

    private Suburb() {}

}
