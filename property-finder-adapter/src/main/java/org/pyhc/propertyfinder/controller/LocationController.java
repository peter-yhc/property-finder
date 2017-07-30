package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.model.LocationsDTO;
import org.pyhc.propertyfinder.suburb.SearchLocationPort;
import org.pyhc.propertyfinder.suburb.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/api/locations")
public class LocationController {

    private SearchLocationPort searchLocationPort;

    @Autowired
    public LocationController(SearchLocationPort searchLocationPort) {
        this.searchLocationPort = searchLocationPort;
    }

    @RequestMapping(produces = {APPLICATION_JSON_VALUE}, method = GET)
    public ResponseEntity<LocationsDTO> getSearchableLocations(@RequestParam(value = "page", required = false) Integer currentPage) {
        Pageable pageable = new PageRequest(currentPage == null ? 0 : currentPage, 20);
        Page<SuburbDetails> searchableLocations = searchLocationPort.getSearchableLocations(pageable);

        LocationsDTO response = new LocationsDTO(searchableLocations);
        response.add(linkTo(methodOn(LocationController.class).getSearchableLocations(currentPage)).withSelfRel());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(consumes = {APPLICATION_JSON_VALUE}, method = POST)
    public ResponseEntity<Void> saveNewSearchLocation(@RequestBody SuburbDetails suburbDetails) {
        searchLocationPort.recordSearch(suburbDetails);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/{uuid}", method = DELETE)
    public ResponseEntity<Void> deleteSearchLocation(@PathVariable UUID uuid) {
        searchLocationPort.removeSavedSearch(uuid);
        return ResponseEntity.ok().build();
    }
}
