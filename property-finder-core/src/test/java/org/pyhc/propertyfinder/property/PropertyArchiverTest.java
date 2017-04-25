package org.pyhc.propertyfinder.property;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.pyhc.propertyfinder.property.model.SoldProperty;
import org.pyhc.propertyfinder.property.model.SoldPropertyRepository;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DatabaseConfiguration.class, PropertyArchiverTest.BeanConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoExecutionListener.class})
public class PropertyArchiverTest {

    @Autowired
    private PropertyArchiver propertyArchiver;

    @Autowired
    private SoldPropertyRepository soldPropertyRepository;

    @Test
    public void canArchiveSoldProperty() {
        SoldPropertyProfile soldPropertyProfile = createRandomSoldPropertyProfile();
        propertyArchiver.archiveSoldProperty(soldPropertyProfile);

        assertThat(soldPropertyRepository.findAll().size(), is(1));

        SoldProperty soldProperty = soldPropertyRepository.findAll().get(0);
        assertThat(soldProperty.getAddress(), is(soldPropertyProfile.getAddress()));
        assertThat(soldProperty.getSuburb(), is(soldPropertyProfile.getSuburb()));
        assertThat(soldProperty.getPostcode(), is(soldPropertyProfile.getPostcode()));
        assertThat(soldProperty.getBeds(), is(soldPropertyProfile.getBed()));
        assertThat(soldProperty.getBaths(), is(soldPropertyProfile.getBath()));
        assertThat(soldProperty.getCars(), is(soldPropertyProfile.getCar()));
        assertThat(soldProperty.getPropertyCode(), is(soldPropertyProfile.getPropertyCode()));
        assertThat(soldProperty.getPropertyLink(), is(soldPropertyProfile.getPropertyLink()));
        assertThat(soldProperty.getSoldDate(), is(soldPropertyProfile.getSoldDate()));
        assertThat(soldProperty.getPrice(), is(soldPropertyProfile.getPrice()));
    }

    @Test
    public void willNotPersistTheSamePropertyTwice() throws Exception {
        SoldPropertyProfile soldPropertyProfile = createRandomSoldPropertyProfile();
        propertyArchiver.archiveSoldProperty(soldPropertyProfile);

        propertyArchiver.archiveSoldProperty(soldPropertyProfile);
        assertThat(soldPropertyRepository.findAll().size(), is(1));
    }

    @Test
    public void shouldUpdateSoldPropertyIfPriceChanges() throws Exception {
        SoldPropertyProfile soldPropertyProfile = createRandomSoldPropertyProfile();
        propertyArchiver.archiveSoldProperty(soldPropertyProfile);

        Field f1 = soldPropertyProfile.getClass().getDeclaredField("price");
        f1.setAccessible(true);
        f1.set(soldPropertyProfile, 100);

        propertyArchiver.archiveSoldProperty(soldPropertyProfile);
        assertThat(soldPropertyRepository.findAll().get(0).getPrice(), is(100));
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
