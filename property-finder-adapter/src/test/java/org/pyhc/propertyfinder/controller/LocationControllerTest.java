package org.pyhc.propertyfinder.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterConfiguration;
import org.pyhc.propertyfinder.suburb.SearchLocationPort;
import org.pyhc.propertyfinder.suburb.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AdapterConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class LocationControllerTest {

    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private SearchLocationPort searchLocationPort;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void canGetSearchableLocations() throws Exception {
        happyCaseMocks(500, 0, 100, 100);
        mockMvc.perform(get("/api/locations"))
            .andDo(print())
            .andExpect(jsonPath("$.locations.length()", is(100)))
            .andExpect(jsonPath("$.locations.[0].name", is("Milsons Point")))
            .andExpect(jsonPath("$.locations.[0].state", is("NSW")))
            .andExpect(jsonPath("$.locations.[0].postcode", is(2061)));
    }

    @Test
    public void searchableLocations_HaveSelfLinks() throws Exception {
        happyCaseMocks(1, 0, 20, 1);
        mockMvc.perform(get("/api/locations"))
            .andDo(print())
            .andExpect(jsonPath("$.links[0].href", is("http://localhost/api/locations{?page}")))
            .andExpect(jsonPath("$.links[0].rel", is("self")));
    }

    @Test
    public void hasPageableLinks() throws Exception {
        happyCaseMocks(1, 0, 20, 1);
        mockMvc.perform(get("/api/locations"))
            .andDo(print())
            .andExpect(jsonPath("$.page.first", is(true)))
            .andExpect(jsonPath("$.page.last", is(true)))
            .andExpect(jsonPath("$.page.totalElements", is(1)))
            .andExpect(jsonPath("$.page.totalPages", is(1)))
            .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    public void canSaveNewSearchLocation() throws Exception {
        SuburbDetails suburbDetails = SuburbDetails.builder().name("Carlton").postcode(3053).state("VIC").build();

        mockMvc.perform(post("/api/locations").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(suburbDetails)))
            .andDo(print())
            .andExpect(status().is(ACCEPTED.value()));

        verify(searchLocationPort).recordSearch(suburbDetails);
    }

    @Test
    public void canDeleteSearchLocation() throws Exception {
        UUID uuid = UUID.randomUUID();
        mockMvc.perform(delete(format("/api/locations/%s", uuid.toString())))
            .andDo(print())
            .andExpect(status().is(OK.value()));

        verify(searchLocationPort).removeSavedSearch(uuid);
    }

    private void happyCaseMocks(int itemCount, int currentPage, int pageSize, int pageCount) {
        Pageable pageable = new PageRequest(currentPage, pageSize);
        SuburbDetails exampleSuburb = SuburbDetails.builder().name("Milsons Point").state("NSW").postcode(2061).build();
        when(searchLocationPort.getSearchableLocations(any())).thenReturn(
            new PageImpl<>(
                Stream.generate(() -> exampleSuburb).limit(pageCount).collect(Collectors.toList()),
                pageable,
                itemCount
            )
        );
    }
}
