package org.pyhc.propertyfinder.controller;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.pyhc.propertyfinder.settings.SearchLocation;
import org.pyhc.propertyfinder.settings.SettingsPort;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsWebTest extends AbstractWebTest {

    @MockBean
    private SettingsPort settingsPort;

    @Override
    public String getWebDriver() {
        return "chrome";
    }

    @Test
    public void canViewSettingsPage() {
        when(settingsPort.getSavedSearches()).thenReturn(asList(
                SearchLocation.builder().suburb("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/" + "settings");

        assertThat(window().title(), is("Property Finder - Settings"));
        assertThat($("#property-finder-brand").text(), is("Property Finder"));

        assertThat($("#pf-search-location-input").present(), is(true));
        assertThat($("#pf-saved-searches-list").present(), is(true));
        assertThat($("#pf-saved-searches-item-0").text(), is("Homebush NSW, 2140"));
        assertThat($("#pf-saved-searches-item-1").text(), is("Strathfield NSW, 2135"));
        assertThat($("#pf-saved-searches-delete-0").present(), is(true));
        assertThat($("#pf-saved-searches-delete-1").present(), is(true));

        verify(settingsPort).getSavedSearches();
    }

    @Test
    public void autocompleteOnSearchBar_ReturnsCorrectMatches() {
        when(settingsPort.getSearchableLocations()).thenReturn(asList(
                SearchLocation.builder().suburb("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Homebush West").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Sydney Olympic Park").postcode(2127).state("NSW").build(),
                SearchLocation.builder().suburb("Auburn").postcode(2144).state("NSW").build(),
                SearchLocation.builder().suburb("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/" + "settings");

        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("home");
        await().atMost(1000).until(() -> $("#ui-id-1").find(".ui-menu-item").size() == 2);

        FluentList<FluentWebElement> autocompleteFields = $("#ui-id-1").find(".ui-menu-item");
        assertThat(autocompleteFields.get(0).text(), is("Homebush NSW, 2140"));
        assertThat(autocompleteFields.get(1).text(), is("Homebush West NSW, 2140"));
    }

    @Test
    public void canUseAutocompleteOnSearchBar_ReturnsNothingForNoMatches() {
        when(settingsPort.getSearchableLocations()).thenReturn(asList(
                SearchLocation.builder().suburb("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Homebush West").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Sydney Olympic Park").postcode(2127).state("NSW").build(),
                SearchLocation.builder().suburb("Auburn").postcode(2144).state("NSW").build(),
                SearchLocation.builder().suburb("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/" + "settings");

        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("nothing matches me");
        await().explicitlyFor(1, TimeUnit.SECONDS);

        assertThat($("#ui-id-1").find(".ui-menu-item").size(), is(0));
    }

    @Test
    public void clickingDeleteButton_ForFirstRow_RemovesSavedSearch() throws Exception {
        when(settingsPort.getSavedSearches()).thenReturn(asList(
                SearchLocation.builder().suburb("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/" + "settings");

        await().until(()-> $("#pf-saved-searches-delete-0").present());
        getDriver().findElement(By.id("pf-saved-searches-delete-0")).sendKeys(Keys.RETURN);

        assertThat($("#pf-saved-searches-item-1").text(), is("Strathfield NSW, 2135"));
        await().until(() -> !$("#pf-saved-searches-item-0").present());

        ArgumentCaptor<SearchLocation> argumentCaptor = ArgumentCaptor.forClass(SearchLocation.class);
        verify(settingsPort).removeSavedLocation(argumentCaptor.capture());

        SearchLocation searchLocation = argumentCaptor.getValue();
        assertThat(searchLocation.getSuburb(), is("Homebush"));
        assertThat(searchLocation.getState(), is("NSW"));
        assertThat(searchLocation.getPostcode(), is(2140));
    }

    @Test
    public void clickingDeleteButton_ForSecondRow_RemovesSavedSearch() throws Exception {
        when(settingsPort.getSavedSearches()).thenReturn(asList(
                SearchLocation.builder().suburb("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburb("Strathfield").postcode(2135).state("NSW").build()
        ));
        doNothing().when(settingsPort).removeSavedLocation(any());

        goTo("http://localhost:" + serverPort + "/" + "settings");

        await().until(()-> $("#pf-saved-searches-delete-1").present());
        getDriver().findElement(By.id("pf-saved-searches-delete-1")).sendKeys(Keys.RETURN);

        assertThat($("#pf-saved-searches-item-0").text(), is("Homebush NSW, 2140"));
        await().until(() -> !$("#pf-saved-searches-item-1").present());

        ArgumentCaptor<SearchLocation> argumentCaptor = ArgumentCaptor.forClass(SearchLocation.class);
        verify(settingsPort).removeSavedLocation(argumentCaptor.capture());

        SearchLocation searchLocation = argumentCaptor.getValue();
        assertThat(searchLocation.getSuburb(), is("Strathfield"));
        assertThat(searchLocation.getState(), is("NSW"));
        assertThat(searchLocation.getPostcode(), is(2135));
    }
}
