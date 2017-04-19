package org.pyhc.propertyfinder.controller;

import org.apache.log4j.Logger;
import org.pyhc.propertyfinder.property.PropertyProcessorPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    private static final Logger LOG = Logger.getLogger(AnalysisController.class);

    @Autowired
    private PropertyProcessorPort propertyProcessorPort;

    @RequestMapping
    public String showAnalysisPage() {

        return "analysis";
    }

    @RequestMapping(path = "/searchSoldProperties", method = RequestMethod.POST)
    public String triggerSoldPropertiesSearch() {
        LOG.info("Search sold properties triggered");
        propertyProcessorPort.searchSoldProperties();
        return "analysis";
    }

}
