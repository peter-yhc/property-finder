package org.pyhc.propertyfinder.settings.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Suburb {

    @Getter(AccessLevel.NONE)
    private String _id;

    private String name;
    private String state;

}
