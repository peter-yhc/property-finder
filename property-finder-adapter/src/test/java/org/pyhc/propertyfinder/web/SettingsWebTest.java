package org.pyhc.propertyfinder.web;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.pyhc.propertyfinder.settings.SearchLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SettingsWebTest extends AbstractWebTest {

    @Test
    public void canViewSettingsPage() {
        when(searchLocationPort.getSavedSearchLocations()).thenReturn(asList(
                SearchLocation.builder().suburbName("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/" + "settings");

        assertThat(window().title(), is("Property Finder - Settings"));
        assertThat($("#property-finder-brand").text(), is("Property Finder"));

        assertThat($("#pf-search-location-input").present(), is(true));
        assertThat($("#pf-saved-searches-list").present(), is(true));
        assertThat($("#pf-saved-searches-item-0").text(), containsString("Homebush NSW, 2140"));
        assertThat($("#pf-saved-searches-item-1").text(), containsString("Strathfield NSW, 2135"));
        assertThat($("#pf-saved-searches-delete-0").present(), is(true));
        assertThat($("#pf-saved-searches-delete-1").present(), is(true));

        verify(searchLocationPort).getSavedSearchLocations();
    }

    @Test
    public void autocompleteOnSearchBar_ReturnsCorrectMatches() {
        when(searchLocationPort.getSearchableLocations()).thenReturn(asList(
                SearchLocation.builder().suburbName("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Homebush West").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Sydney Olympic Park").postcode(2127).state("NSW").build(),
                SearchLocation.builder().suburbName("Auburn").postcode(2144).state("NSW").build(),
                SearchLocation.builder().suburbName("Strathfield").postcode(2135).state("NSW").build()
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
        when(searchLocationPort.getSearchableLocations()).thenReturn(asList(
                SearchLocation.builder().suburbName("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Homebush West").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Sydney Olympic Park").postcode(2127).state("NSW").build(),
                SearchLocation.builder().suburbName("Auburn").postcode(2144).state("NSW").build(),
                SearchLocation.builder().suburbName("Strathfield").postcode(2135).state("NSW").build()
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
        when(searchLocationPort.getSavedSearchLocations()).thenReturn(asList(
                SearchLocation.builder().suburbName("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/" + "settings");

        await().until(() -> $("#pf-saved-searches-delete-0").present());
        clickElement("pf-saved-searches-delete-0");

        assertThat($("#pf-saved-searches-item-1").text(), containsString("Strathfield NSW, 2135"));
        await().until(() -> !$("#pf-saved-searches-item-0").present());

        ArgumentCaptor<SearchLocation> argumentCaptor = ArgumentCaptor.forClass(SearchLocation.class);
        verify(searchLocationPort).removeSavedLocation(argumentCaptor.capture());

        SearchLocation searchLocation = argumentCaptor.getValue();
        assertThat(searchLocation.getSuburbName(), is("Homebush"));
        assertThat(searchLocation.getState(), is("NSW"));
        assertThat(searchLocation.getPostcode(), is(2140));
    }

    @Test
    public void clickingDeleteButton_ForSecondRow_RemovesSavedSearch() throws Exception {
        when(searchLocationPort.getSavedSearchLocations()).thenReturn(asList(
                SearchLocation.builder().suburbName("Homebush").postcode(2140).state("NSW").build(),
                SearchLocation.builder().suburbName("Strathfield").postcode(2135).state("NSW").build()
        ));
        doNothing().when(searchLocationPort).removeSavedLocation(any());

        goTo("http://localhost:" + serverPort + "/" + "settings");

        await().until(() -> $("#pf-saved-searches-delete-1").present());
        clickElement("pf-saved-searches-delete-1");

        assertThat($("#pf-saved-searches-item-0").text(), containsString("Homebush NSW, 2140"));
        await().until(() -> !$("#pf-saved-searches-item-1").present());

        ArgumentCaptor<SearchLocation> argumentCaptor = ArgumentCaptor.forClass(SearchLocation.class);
        verify(searchLocationPort).removeSavedLocation(argumentCaptor.capture());

        SearchLocation searchLocation = argumentCaptor.getValue();
        assertThat(searchLocation.getSuburbName(), is("Strathfield"));
        assertThat(searchLocation.getState(), is("NSW"));
        assertThat(searchLocation.getPostcode(), is(2135));
    }

    @Test
    public void canAddNewSavedLocation_AndReloadPage() throws Exception {
        when(searchLocationPort.getSavedSearchLocations())
                .thenReturn(emptyList())
                .thenReturn(singletonList(
                        SearchLocation.builder().suburbName("North Strathfield").postcode(2067).state("NSW").build()
                ));
        doNothing().when(searchLocationPort).addSavedLocation(any());

        goTo("http://localhost:" + serverPort + "/" + "settings");

        assertThat($("#pf-saved-searches-item-0").present(), is(false));

        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("North Strathfield NSW, 2067");

        $("#pf-search-location-add").click();

        ArgumentCaptor<SearchLocation> argumentCaptor = ArgumentCaptor.forClass(SearchLocation.class);
        verify(searchLocationPort).addSavedLocation(argumentCaptor.capture());

        SearchLocation searchLocation = argumentCaptor.getValue();
        assertThat(searchLocation.getSuburbName(), is("North Strathfield"));
        assertThat(searchLocation.getState(), is("NSW"));
        assertThat(searchLocation.getPostcode(), is(2067));

        await().until(() -> $("#pf-saved-searches-item-0").present());
    }

    @Test
    @Ignore
    public void showError_WhenAddingInvalidSearchLocation() throws Exception {
        goTo("http://localhost:" + serverPort + "/" + "settings");
        assertThat(getDriver().findElement(By.id("pf-saved-searches-error")).getCssValue("display"), is("none"));

        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("blah blah wrong format");

        $("#pf-search-location-add").click();
        assertThat(getDriver().findElement(By.id("pf-saved-searches-error")).getCssValue("display"), is("block"));
        assertThat($("#pf-saved-searches-error").text(), is("Format should be 'Suburb State, PostCode' (ex. Sydney NSW, 2000)"));
        verify(searchLocationPort, times(0)).addSavedLocation(any());
    }

    static class SelectorOptionsMatcher extends TypeSafeMatcher<List<WebElement>> {

        private List<String> options;

        private SelectorOptionsMatcher(String... options) {
            this.options = new ArrayList<>(asList(options));
        }

        @Override
        protected boolean matchesSafely(List<WebElement> elements) {
            List<String> elementTextValues = elements.stream().map(WebElement::getText).collect(toList());
            options.removeAll(elementTextValues);
            return options.size() == 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("should include option: " + options.stream().collect(joining(",")));
        }

        @Override
        protected void describeMismatchSafely(List<WebElement> items, Description mismatchDescription) {
            mismatchDescription.appendText("selector contained options: " + items.stream().map(WebElement::getText).collect(joining(",")));
        }

        static SelectorOptionsMatcher hasSelectorOptions(String... options) {
            return new SelectorOptionsMatcher(options);
        }
    }
}
