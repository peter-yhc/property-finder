package org.pyhc.propertyfinder.scraper;

import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ScraperResultPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishProfileResult(SoldPropertyProfile soldPropertyProfile) {

    }
}
