package org.pyhc.propertyfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SettingsController {

    @RequestMapping("/settings")
    public String settingHome(Model model) {
        return "settings";
    }

}
