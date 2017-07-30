package org.pyhc.propertyfinder.suburb;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
public class SuburbDetails {

    @NotNull
    private String name;

    @NotNull
    private String state;

    @NotNull
    private Integer postcode;

    @NotNull
    private UUID uuid;

}
