package org.pyhc.propertyfinder.controller;

import org.junit.Test;
import org.openqa.selenium.By;
import org.pyhc.propertyfinder.property.PropertyProcessorPort;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.pyhc.propertyfinder.controller.SettingsWebTest.SelectorOptionsMatcher.hasSelectorOptions;

public class AnalysisWebTest extends AbstractWebTest {

    @MockBean
    private PropertyProcessorPort propertyProcessorPort;

    @Test
    public void hasConfigurationOptions() {
        goTo("http://localhost:" + serverPort + "/analysis");

        assertThat($("#pf-beds-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Studio", "1", "2", "3", "4", "5"));
        assertThat($("#pf-bathrooms-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Any", "1+", "2+", "3+", "4+", "5+"));
        assertThat($("#pf-cars-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Any", "1+", "2+", "3+", "4+", "5+"));
    }

    @Test
    public void canTriggerProcessorToSearchForSoldProperties() {
        goTo("http://localhost:" + serverPort + "/analysis");

        $("#pf-search-sold-properties-button").click();

        verify(propertyProcessorPort).searchSoldProperties();
    }

}
