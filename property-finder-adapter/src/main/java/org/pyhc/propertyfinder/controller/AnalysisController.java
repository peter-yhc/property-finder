package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.model.PreviousSearchDTO;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController("/analysis")
public class AnalysisController {

    private final SearchLocationPort searchLocationPort;

    @Autowired
    public AnalysisController(SearchLocationPort searchLocationPort) {
        this.searchLocationPort = searchLocationPort;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PreviousSearchDTO> getLocationsAnalysed() {
        List<SuburbDetails> previousSearches = searchLocationPort.getPreviousSearches();
        PreviousSearchDTO response = new PreviousSearchDTO(previousSearches);
        response.add(linkTo(methodOn(AnalysisController.class).getLocationsAnalysed()).withRel("next"));
        return ResponseEntity.ok(response);
    }

//
//    @ResponseBody
//    @RequestMapping(value = "/analysis/locations", method = RequestMethod.POST)
//    public ResponseEntity<Void> recordSearch(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm) {
//        searchLocationPort.recordSearch(searchLocationForm.parseData());
//        return ResponseEntity.ok().build();
//    }
//
//    @RequestMapping(value = "/analysis/locations/{searchUuid}", method = RequestMethod.DELETE)
//    @ResponseBody
//    public ResponseEntity<Void> removeSavedLocation(@PathVariable("searchUuid") @NotNull @Valid UUID searchUuid) {
//        searchLocationPort.removeSavedLocation(searchUuid);
//        return ResponseEntity.ok().build();
//    }

}
