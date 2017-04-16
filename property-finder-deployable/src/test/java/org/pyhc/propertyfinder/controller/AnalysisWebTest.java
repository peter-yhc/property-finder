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

        assertThat($("#pf-min-beds-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Studio", "1", "2", "3", "4", "5"));
        assertThat($("#pf-max-beds-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Studio", "1", "2", "3", "4", "5"));
        assertThat($("#pf-bathrooms-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Any", "1+", "2+", "3+", "4+", "5+"));
        assertThat($("#pf-cars-selector").find(By.tagName("option")).toElements(), hasSelectorOptions("Any", "1+", "2+", "3+", "4+", "5+"));
        assertThat($("#pf-min-price-selector").find(By.tagName("option")).toElements(), hasSelectorOptions(
                "Any", "200,000", "300,000", "400,000", "500,000", "600,000", "700,000", "800,000", "900,000",
                "1,000,000", "1,250,000", "1,500,000", "2,000,000", "2,500,000", "3,000,000", "4,000,000", "5,000,000",
                "10,000,000"
        ));
        assertThat($("#pf-max-price-selector").find(By.tagName("option")).toElements(), hasSelectorOptions(
                "Any", "200,000", "300,000", "400,000", "500,000", "600,000", "700,000", "800,000", "900,000",
                "1,000,000", "1,250,000", "1,500,000", "2,000,000", "2,500,000", "3,000,000", "4,000,000", "5,000,000",
                "10,000,000"
        ));
    }

    @Test
    public void canTriggerProcessorToSearchForSoldProperties() {
        goTo("http://localhost:" + serverPort + "/analysis");

        $("#pf-search-sold-properties-button").click();

        verify(propertyProcessorPort).searchSoldProperties();
    }

}
