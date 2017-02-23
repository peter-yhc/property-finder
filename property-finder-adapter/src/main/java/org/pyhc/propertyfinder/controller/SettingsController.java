package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SettingsPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SettingsController {

    @Autowired
    private SettingsPort settingsPort;

    @RequestMapping("/settings")
    public String settingHome(Model model) {

        List<SearchLocation> searchLocations = settingsPort.getSearchLocations();

        model.addAttribute("savedSearches", searchLocations);

        return "settings/settings";
    }

}
