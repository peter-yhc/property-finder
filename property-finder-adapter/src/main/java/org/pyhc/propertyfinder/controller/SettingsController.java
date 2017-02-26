package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.controller.form.SearchLocationForm;
import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SettingsPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SettingsController {

    @Autowired
    private SettingsPort settingsPort;

    @RequestMapping("/settings")
    public String settingHome(Model model) {
        List<SearchLocation> searchLocations = settingsPort.getSavedSearches();

        model.addAttribute("savedSearches", searchLocations);
        model.addAttribute("searchLocationForm", new SearchLocationForm());
        return "settings/settings";
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getSearchableLocations() {
        List<SearchLocation> searchableLocations = settingsPort.getSearchableLocations();
        return searchableLocations
                .stream()
                .map(SearchLocation::toString)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.POST)
    public String addSavedLocation(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm) {
        settingsPort.addSavedLocation(searchLocationForm.parseData());
        return "settings/settings";
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> removeSavedLocation(@RequestBody @NotNull @Valid SearchLocation searchLocation) {
        settingsPort.removeSavedLocation(searchLocation);
        return ResponseEntity.ok().build();
    }

}
