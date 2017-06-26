package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.form.SearchLocationForm;
import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class AnalysisController {

    @Autowired
    private SearchLocationPort searchLocationPort;

    @RequestMapping("/analysis")
    public String settingHome(Model model) {
        List<SearchLocation> searchLocations = searchLocationPort.getSavedSearchLocations();

        model.addAttribute("savedSearches", searchLocations);
        model.addAttribute("searchLocationForm", new SearchLocationForm());
        return "analysis";
    }

    @RequestMapping(value = "/analysis/locations", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getSearchableLocations() {
        List<SearchLocation> searchableLocations = searchLocationPort.getSearchableLocations();
        return searchableLocations
                .stream()
                .map(SearchLocation::toString)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/analysis/locations", method = RequestMethod.POST)
    public String addSavedLocation(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm) {
        searchLocationPort.addSavedLocation(searchLocationForm.parseData());
        return "redirect:/analysis";
    }

    @RequestMapping(value = "/analysis/locations/{savedSearchId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> removeSavedLocation(@PathVariable("savedSearchId") @NotNull @Valid UUID savedSearchId) {
        searchLocationPort.removeSavedLocation(savedSearchId);
        return ResponseEntity.ok().build();
    }

}
