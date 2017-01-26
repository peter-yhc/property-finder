package org.pyhc.propertyfinder.configuration;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ContextConfiguration(classes = {
        TestAdapterConfiguration.class
})
@WebAppConfiguration
public @interface AdapterTest {
}
