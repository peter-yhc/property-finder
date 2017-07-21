package org.pyhc.propertyfinder.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pyhc.propertyfinder.settings.SuburbDetails;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LocationsDTO extends PageResource {
    List<SuburbDetails> locations;

    public LocationsDTO(List<SuburbDetails> locations) {
        this.locations = locations;
        setPagination(locations);
    }
}
