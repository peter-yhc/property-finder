package org.pyhc.propertyfinder.controller;

import org.pyhc.propertyfinder.property.PropertyProcessorPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private PropertyProcessorPort propertyProcessorPort;

    @RequestMapping
    public String showAnalysisPage() {

        return "analysis";
    }

    @RequestMapping(path = "/searchSoldProperties", method = RequestMethod.POST)
    public String triggerSoldPropertiesSearch() {
        propertyProcessorPort.searchSoldProperties();
        return "analysis";
    }

}
