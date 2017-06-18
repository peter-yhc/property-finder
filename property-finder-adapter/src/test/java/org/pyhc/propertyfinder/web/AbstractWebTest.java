package org.pyhc.propertyfinder.web;

import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.pyhc.propertyfinder.configuration.AdapterConfiguration;
import org.pyhc.propertyfinder.property.PropertyArchiverPort;
import org.pyhc.propertyfinder.property.PropertyProcessorPort;
import org.pyhc.propertyfinder.scraper.CompletableRestTemplate;
import org.pyhc.propertyfinder.settings.SearchLocationPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                AbstractWebTest.TestApplication.class,
                AdapterConfiguration.class
        })
public abstract class AbstractWebTest extends FluentTest {

    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\webdrivers\\chromedriver.exe");
    }

    @MockBean
    protected CompletableRestTemplate completableRestTemplate;

    @MockBean
    protected PropertyArchiverPort propertyArchiverPort;

    @MockBean
    protected SearchLocationPort searchLocationPort;

    @MockBean
    protected PropertyProcessorPort propertyProcessorPort;

    @LocalServerPort
    protected String serverPort;

    @Override
    public String getWebDriver() {
        return "chrome";
    }

    void clickElement(String element) {
        Actions action = new Actions(getDriver());
        action.moveToElement(getDriver().findElement(By.id(element)));
        action.click();
        action.sendKeys(Keys.RETURN);
        action.build().perform();
    }

    @SpringBootApplication
    public static class TestApplication {

        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
        }
    }

}
