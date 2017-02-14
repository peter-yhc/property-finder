package org.pyhc.propertyfinder.configuration;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ContextConfiguration(classes = {
        TestCoreConfiguration.class
})
public @interface CoreTest {
}
