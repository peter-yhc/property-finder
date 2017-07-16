package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.model.PreviousSearchDTO;
import org.pyhc.propertyfinder.controller.model.SearchLocationForm;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("/analysis")
public class AnalysisController {

    @Autowired
    private SearchLocationPort searchLocationPort;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PreviousSearchDTO> getLocationsAnalysed() {
        List<SuburbDetails> previousSearches = searchLocationPort.getPreviousSearches();
        return ResponseEntity.ok(new PreviousSearchDTO(previousSearches));
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
