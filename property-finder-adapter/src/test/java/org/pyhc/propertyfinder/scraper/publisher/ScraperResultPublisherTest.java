package org.pyhc.propertyfinder.scraper.publisher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pyhc.propertyfinder.events.ProfileResultEvent;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScraperResultPublisherTest {

    @InjectMocks
    private ScraperResultPublisher scraperResultPublisher;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void canPublishNewProfileResultEvent() {
        SoldPropertyProfile soldPropertyProfile = SoldPropertyProfile.builder().build();
        scraperResultPublisher.publishProfileResult(soldPropertyProfile);

        verify(applicationEventPublisher).publishEvent(new ProfileResultEvent(soldPropertyProfile));
    }

}