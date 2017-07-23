package org.pyhc.propertyfinder.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pyhc.propertyfinder.configuration.DatabaseConfiguration;
import org.pyhc.propertyfinder.configuration.MongoExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DatabaseConfiguration.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, MongoExecutionListener.class})
public class SuburbRepositoryTest {

    @Autowired
    private SuburbRepository suburbRepository;

    @Test
    public void canGetPageableResults() {
        saveRandomSuburbs(10);
        Pageable pageable = new PageRequest(0, 5);
        Page<Suburb> page = suburbRepository.findAll(pageable);

        assertThat(page.getTotalElements(), is(10L));
        assertThat(page.getTotalPages(), is(2));
    }

    @Test
    public void canGetNextPage() {
        saveRandomSuburbs(10);

        Pageable pageable1 = new PageRequest(0, 5);
        Page<Suburb> page1 = suburbRepository.findAll(pageable1);
        assertThat(page1.isFirst(), is(true));

        Pageable pageable2 = page1.nextPageable();
        Page<Suburb> page2 = suburbRepository.findAll(pageable2);
        assertThat(page2.isFirst(), is(false));
    }

    @Test
    public void pageElementsMatchDatabaseCount() {
        saveRandomSuburbs(10);

        Pageable pageable2 = new PageRequest(1, 5);
        Page<Suburb> page2 = suburbRepository.findAll(pageable2);

        List<Suburb> suburbs = page2.getContent();
        assertThat(suburbs.size(), is(5));
        assertThat(suburbs.get(0).getPostcode(), is(5));
        assertThat(suburbs.get(1).getPostcode(), is(6));
    }

    @Test
    public void canGetLargePage() {
        saveRandomSuburbs(100);

        Pageable pageable = new PageRequest(1, 100);
        Page<Suburb> page = suburbRepository.findAll(pageable);

        assertThat(page.getTotalPages(), is(1));
        assertThat(page.getTotalElements(), is(100L));
    }

    @Test
    public void returnsZeroElementsWhenPageShouldntExist() {
        saveRandomSuburbs(5);

        Pageable pageable = new PageRequest(2, 10);
        Page<Suburb> page = suburbRepository.findAll(pageable);
        assertThat(page.getContent().size(), is(0));
    }

    private void saveRandomSuburbs(int count) {
        List<Suburb> suburbs = Stream.generate(randomSuburb())
            .limit(count)
            .collect(Collectors.toList());
        suburbRepository.save(suburbs);
    }

    private static Supplier<Suburb> randomSuburb() {
        final int[] count = {0};
        return () -> Suburb.builder()
            .name(RandomStringUtils.randomAlphabetic(10))
            .postcode(count[0]++)
            .state(RandomStringUtils.randomAlphabetic(3))
            .build();
    }
}