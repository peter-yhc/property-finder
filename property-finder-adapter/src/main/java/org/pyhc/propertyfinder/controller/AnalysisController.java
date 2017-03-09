package org.pyhc.propertyfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    @RequestMapping
    public String showAnalysisPage() {

        return "analysis";
    }

}
