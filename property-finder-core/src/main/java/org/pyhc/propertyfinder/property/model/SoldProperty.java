package org.pyhc.propertyfinder.property.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
public class SoldProperty {

    @Getter(AccessLevel.NONE)
    private String _id;

    private Integer price;
    private String address;
    private Integer beds;
    private Integer baths;
    private Integer cars;
    private String suburb;
    private Integer postcode;
    private Integer propertyCode;
    private String propertyLink;
    private LocalDate soldDate;

    @Builder
    public SoldProperty(Integer price,
                        String address,
                        Integer beds,
                        Integer baths,
                        Integer cars,
                        String suburb,
                        Integer postcode,
                        Integer propertyCode,
                        String propertyLink,
                        LocalDate soldDate) {
        this.price = price;
        this.address = address;
        this.beds = beds;
        this.baths = baths;
        this.cars = cars;
        this.suburb = suburb;
        this.postcode = postcode;
        this.propertyCode = propertyCode;
        this.propertyLink = propertyLink;
        this.soldDate = soldDate;
    }

    public String getId() {
        return this._id;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
