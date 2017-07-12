package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.model.LocationsDTO;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/locations")
public class LocationController {

    private SearchLocationPort searchLocationPort;

    @Autowired
    public LocationController(SearchLocationPort searchLocationPort) {
        this.searchLocationPort = searchLocationPort;
    }

    @RequestMapping(produces = {APPLICATION_JSON_VALUE}, method = GET)
    public ResponseEntity<LocationsDTO> getSearchableLocations() {
        List<SuburbDetails> searchableLocations = searchLocationPort.getSearchableLocations();

        return ResponseEntity.ok(new LocationsDTO(searchableLocations));
    }
}
