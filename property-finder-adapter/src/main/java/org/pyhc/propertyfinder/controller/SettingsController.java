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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SettingsController {

    @Autowired
    private SearchLocationPort searchLocationPort;

    private static final List<SearchLocation> manualList = Arrays.asList(
            SearchLocation.builder().suburbName("Strathfield").state("NSW").postcode(2000).build(),
            SearchLocation.builder().suburbName("Gladesville").state("NSW").postcode(2111).build(),
            SearchLocation.builder().suburbName("Richmond").state("NSW").postcode(2753).build(),
            SearchLocation.builder().suburbName("Peakhurst").state("NSW").postcode(2210).build(),
            SearchLocation.builder().suburbName("Caringbah South").state("NSW").postcode(2229).build(),
            SearchLocation.builder().suburbName("Cecil Hills").state("NSW").postcode(2171).build()
    );

    @RequestMapping("/settings")
    public String settingHome(Model model) {
        List<SearchLocation> searchLocations = searchLocationPort.getSavedSearchLocations();

        model.addAttribute("savedSearches", searchLocations);
        model.addAttribute("searchLocationForm", new SearchLocationForm());
        return "settings";
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getSearchableLocations() {
        List<SearchLocation> searchableLocations = searchLocationPort.getSearchableLocations();
        searchableLocations.addAll(manualList);
        return searchableLocations
                .stream()
                .map(SearchLocation::toString)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.POST)
    public String addSavedLocation(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm) {
        searchLocationPort.addSavedLocation(searchLocationForm.parseData());
        return "redirect:/settings";
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> removeSavedLocation(@RequestBody @NotNull @Valid SearchLocation searchLocation) {
        searchLocationPort.removeSavedLocation(searchLocation);
        return ResponseEntity.ok().build();
    }

}
