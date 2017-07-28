package org.pyhc.propertyfinder.web;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.pyhc.propertyfinder.suburb.SuburbDetails;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AnalysisWebTest extends AbstractWebTest {

    @Test
    public void canViewSettingsPage() {
        when(searchLocationPort.getPreviousSearches()).thenReturn(asList(
                SuburbDetails.builder().name("Homebush").postcode(2140).state("NSW").build(),
                SuburbDetails.builder().name("Strathfield").postcode(2135).state("NSW").build()
        ));

        goTo("http://localhost:" + serverPort + "/analysis");

        assertThat(window().title(), is("Property Finder - Settings"));

        assertThat($("#pf-search-location-input").present(), is(true));
        assertThat($("#pf-saved-searches-list").present(), is(true));
        assertThat($("#pf-saved-searches-item-0").text(), containsString("Homebush NSW, 2140"));
        assertThat($("#pf-saved-searches-item-1").text(), containsString("Strathfield NSW, 2135"));
        assertThat($("#pf-saved-searches-delete-0").present(), is(true));
        assertThat($("#pf-saved-searches-delete-1").present(), is(true));

        verify(searchLocationPort).getPreviousSearches();
    }

    @Test
    public void autocompleteOnSearchBar_ReturnsCorrectMatches() {
        List<SuburbDetails> ts = asList(
                SuburbDetails.builder().name("Homebush").postcode(2140).state("NSW").build(),
                SuburbDetails.builder().name("Homebush West").postcode(2140).state("NSW").build(),
                SuburbDetails.builder().name("Sydney Olympic Park").postcode(2127).state("NSW").build(),
                SuburbDetails.builder().name("Auburn").postcode(2144).state("NSW").build(),
                SuburbDetails.builder().name("Strathfield").postcode(2135).state("NSW").build()
        );
        when(searchLocationPort.getSearchableLocations(any())).thenReturn(new PageImpl<>(ts));

        goTo("http://localhost:" + serverPort + "/analysis");

        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("Home");
        await().atMost(1000).until(() -> $(".autocomplete-content").tagNames().size() != 0);

        FluentList<FluentWebElement> autocompleteFields = $(".autocomplete-content").find("li");
        assertThat(autocompleteFields.get(0).text(), is("Homebush, NSW 2140"));
        assertThat(autocompleteFields.get(1).text(), is("Homebush West, NSW 2140"));
    }


    @Test
    public void formCorrectlyParsesData_UponAddingNewSearchLocation() throws Exception {
        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("North Strathfield, NSW 2067");

        $("#pf-search-location-add").click();

        ArgumentCaptor<SuburbDetails> argumentCaptor = ArgumentCaptor.forClass(SuburbDetails.class);
        verify(searchLocationPort).recordSearch(argumentCaptor.capture());

        SuburbDetails suburbDetails = argumentCaptor.getValue();
        assertThat(suburbDetails.getName(), is("North Strathfield"));
        assertThat(suburbDetails.getState(), is("NSW"));
        assertThat(suburbDetails.getPostcode(), is(2067));
    }

    @Test
    public void showError_WhenAddingInvalidSearchLocation() throws Exception {
        goTo("http://localhost:" + serverPort + "/analysis");
        assertThat($("#pf-search-location-input").attribute("class").contains("invalid"), is(false));

        WebElement searchInput = getDriver().findElement(By.id("pf-search-location-input"));
        searchInput.click();
        searchInput.sendKeys("blah blah wrong format");

        $("#pf-search-location-add").click();
        assertThat($("#pf-search-location-input").attribute("class").contains("invalid"), is(true));
        assertThat($("#pf-search-location-input-label").attribute("data-error"), is("Format should be 'Suburb, State PostCode' (ex. Sydney, NSW 2000)"));
        verify(searchLocationPort, times(0)).recordSearch(any());
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
