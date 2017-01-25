package org.pyhc.model;

import javax.persistence.*;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String propertyId;
    private String href;
    private Integer priceMin;
    private Integer priceMax;
    private String address;
    private Integer beds;
    private Integer baths;
    private Integer cars;
    private Integer estimatedPrice;

    @ManyToOne
    private Suburb suburb;

}
