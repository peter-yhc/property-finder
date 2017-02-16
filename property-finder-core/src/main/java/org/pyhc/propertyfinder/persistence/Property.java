package org.pyhc.propertyfinder.persistence;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Property {

    @Getter(AccessLevel.NONE)
    private String _id;

    private String propertyCode;
    private String href;
    private Integer priceMin;
    private Integer priceMax;
    private String address;
    private Integer beds;
    private Integer baths;
    private Integer cars;
    private Integer estimatedPrice;
    private String suburb;

    public String getId() {
        return this._id;
    }
}
