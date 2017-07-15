package org.pyhc.propertyfinder.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@AllArgsConstructor
public class LocationsDTO extends ResourceSupport {
    List<SuburbDetails> locations;
}
