package org.pyhc.propertyfinder.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.AdapterConfiguration;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.pyhc.propertyfinder.settings.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AdapterConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class LocationControllerTest {

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
        happyCaseMocks();
        mockMvc.perform(get("/api/locations"))
                .andDo(print())
                .andExpect(jsonPath("$.locations.length()", is(1)));
    }

    @Test
    public void searchableLocations_HaveSelfLinks() throws Exception {
        happyCaseMocks();
        mockMvc.perform(get("/api/locations"))
                .andDo(print())
                .andExpect(jsonPath("$.links[0].href", is("http://localhost/api/locations")));
    }

    private void happyCaseMocks() {
        when(searchLocationPort.getSearchableLocations()).thenReturn(
                singletonList(SuburbDetails.builder().suburbName("Milsons Point").state("NSW").postcode(2061).build())
        );
    }
}