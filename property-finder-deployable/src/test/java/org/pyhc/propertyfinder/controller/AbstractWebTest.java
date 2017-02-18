package org.pyhc.propertyfinder.controller;

import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.RealestateScraperApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RealestateScraperApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractWebTest extends FluentTest {

    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\webdrivers\\chromedriver.exe");
    }

}
