package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SettingsPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

        return "settings/settings";
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getSearchableLocations() {
        List<SearchLocation> searchableLocations = settingsPort.getSearchableLocations();
        List<String> searchableLocationsText = searchableLocations
                .stream()
                .map(SearchLocation::toString)
                .collect(Collectors.toList());
        return searchableLocationsText;
    }

    @RequestMapping(value = "/settings/locations", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> removeSavedLocation(@RequestBody @NotNull SearchLocation searchLocation) {
        settingsPort.removeSavedLocation(searchLocation);

        return ResponseEntity.ok().build();
    }

}
