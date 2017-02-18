package org.pyhc.propertyfinder.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.context.embedded.LocalServerPort;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SettingsWebTest extends AbstractWebTest {

    @LocalServerPort
    private String serverPort;

    @Override
    public String getWebDriver() {
        return "chrome";
    }

    @Test
    public void canViewSettingsPage() {
        goTo("http://localhost:" + serverPort + "/" + "settings");

        assertThat(window().title(), is("Property Finder - Settings"));
    }
}
