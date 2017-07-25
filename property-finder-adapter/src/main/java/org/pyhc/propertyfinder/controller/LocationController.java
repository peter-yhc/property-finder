package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.model.LocationsDTO;
import org.pyhc.propertyfinder.suburb.SearchLocationPort;
import org.pyhc.propertyfinder.suburb.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
//        List<SuburbDetails> searchableLocations = searchLocationPort.getSearchableLocations();
//        LocationsDTO response = new LocationsDTO(searchableLocations);
//        response.add(linkTo(methodOn(LocationController.class).getSearchableLocations(null)).withSelfRel());
//        response.add(getPageLink(currentPage, response));
//        return ResponseEntity.ok(response);
        return ResponseEntity.ok().build();
    }

    private Link getPageLink(Integer currentPage, LocationsDTO response) {

        if (currentPage == null) {
            currentPage = 1;
        }
        if (response.getTotalElements() > currentPage * response.getPageSize()) {
            currentPage++;
        } else {
            return null;
        }
        String nextPage = linkTo(methodOn(LocationController.class).getSearchableLocations(currentPage)).toString();
        return new Link(nextPage, "nextPage");
    }
}
