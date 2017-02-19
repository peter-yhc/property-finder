package org.pyhc.propertyfinder.controller;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SettingsWebTest extends AbstractWebTest {

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
