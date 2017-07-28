package org.pyhc.propertyfinder.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pyhc.propertyfinder.suburb.SuburbDetails;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LocationsDTO extends PageResource {
    List<SuburbDetails> locations;

    public LocationsDTO(org.springframework.data.domain.Page<SuburbDetails> locations) {
        this.locations = locations.getContent();
        setPagination(locations);
    }
}
