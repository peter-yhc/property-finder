package org.pyhc.propertyfinder.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.events.ProfileResultEvent;
import org.pyhc.propertyfinder.model.PropertyProfile;
import org.pyhc.propertyfinder.model.PropertyProfileRepository;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DatabaseConfiguration.class, PropertyArchiverTest.BeanConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoExecutionListener.class})
public class PropertyArchiverTest {

    @Autowired
    private PropertyArchiver propertyArchiver;

    @Autowired
    private PropertyProfileRepository propertyProfileRepository;

    @Test
    public void canArchiveSoldProperty() {
        ProfileResultEvent profileResultEvent = new ProfileResultEvent(createRandomSoldPropertyProfile());
        propertyArchiver.archiveSoldProperty(profileResultEvent);

        assertThat(propertyProfileRepository.findAll().size(), is(1));

        SoldPropertyProfile soldPropertyProfile = profileResultEvent.getSoldPropertyProfile();
        PropertyProfile propertyProfile = propertyProfileRepository.findAll().get(0);
        assertThat(propertyProfile.getAddress(), is(soldPropertyProfile.getAddress()));
        assertThat(propertyProfile.getSuburb(), is(soldPropertyProfile.getSuburb()));
        assertThat(propertyProfile.getPostcode(), is(soldPropertyProfile.getPostcode()));
        assertThat(propertyProfile.getBeds(), is(soldPropertyProfile.getBed()));
        assertThat(propertyProfile.getBaths(), is(soldPropertyProfile.getBath()));
        assertThat(propertyProfile.getCars(), is(soldPropertyProfile.getCar()));
        assertThat(propertyProfile.getPropertyCode(), is(soldPropertyProfile.getPropertyCode()));
        assertThat(propertyProfile.getPropertyLink(), is(soldPropertyProfile.getPropertyLink()));
        assertThat(propertyProfile.getSoldDate(), is(soldPropertyProfile.getSoldDate()));
        assertThat(propertyProfile.getPrice(), is(soldPropertyProfile.getPrice()));
    }

    @Test
    public void willNotPersistTheSamePropertyTwice() throws Exception {
        ProfileResultEvent profileResultEvent = new ProfileResultEvent(createRandomSoldPropertyProfile());
        propertyArchiver.archiveSoldProperty(profileResultEvent);

        propertyArchiver.archiveSoldProperty(profileResultEvent);
        assertThat(propertyProfileRepository.findAll().size(), is(1));
    }

    @Test
    public void shouldUpdateSoldPropertyIfPriceChanges() throws Exception {
        SoldPropertyProfile soldPropertyProfile = createRandomSoldPropertyProfile();
        propertyArchiver.archiveSoldProperty(new ProfileResultEvent(soldPropertyProfile));

        Field f1 = soldPropertyProfile.getClass().getDeclaredField("price");
        f1.setAccessible(true);
        f1.set(soldPropertyProfile, 100);

        propertyArchiver.archiveSoldProperty(new ProfileResultEvent(soldPropertyProfile));
        assertThat(propertyProfileRepository.findAll().get(0).getPrice(), is(100));
    }

    private SoldPropertyProfile createRandomSoldPropertyProfile() {
        return SoldPropertyProfile.builder()
                .address(randomAlphabetic(10))
                .suburb(randomAlphabetic(4))
                .postcode(nextInt())
                .bed(nextInt())
                .bath(nextInt())
                .car(nextInt())
                .price(nextInt(400000, 1000000))
                .propertyLink(randomAlphabetic(30))
                .propertyCode(nextInt(200000000, 300000000))
                .build();
    }

    @Configuration
    static class BeanConfiguration {

        @Bean
        public PropertyArchiver propertyArchiver() {
            return new PropertyArchiver();
        }
    }
}
