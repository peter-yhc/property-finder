package org.pyhc.propertyfinder.web;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.pyhc.propertyfinder.property.AnalysisToolPort;
import org.pyhc.propertyfinder.property.PropertyProcessorPort;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

public class AnalysisWebTest extends AbstractWebTest {

    @Test
    public void hasConfigurationOptions() {
        goTo("http://localhost:" + serverPort + "/analysis");

        assertThat($("#pf-beds-selector").find(By.tagName("option")).toElements(), SettingsWebTest.SelectorOptionsMatcher.hasSelectorOptions("Studio", "1", "2", "3", "4", "5"));
        assertThat($("#pf-bathrooms-selector").find(By.tagName("option")).toElements(), SettingsWebTest.SelectorOptionsMatcher.hasSelectorOptions("Any", "1+", "2+", "3+", "4+", "5+"));
        assertThat($("#pf-cars-selector").find(By.tagName("option")).toElements(), SettingsWebTest.SelectorOptionsMatcher.hasSelectorOptions("Any", "1+", "2+", "3+", "4+", "5+"));
    }

    @Test
    public void canTriggerProcessorToSearchForSoldProperties() {
        goTo("http://localhost:" + serverPort + "/analysis");

        $("#pf-search-sold-properties-button").click();

        verify(propertyProcessorPort).searchSoldProperties();
    }

    @Test
    @Ignore
    public void shouldDisplayAveragedPriceOverTime() {
        goTo("http://localhost:" + serverPort + "/analysis");

        await().atMost(5, TimeUnit.SECONDS).until($("#chartchartchart")).present();

//        verify(analysisToolPort).averagePriceOverTime(suburb, beds, bathrooms, cars);
    }
}
