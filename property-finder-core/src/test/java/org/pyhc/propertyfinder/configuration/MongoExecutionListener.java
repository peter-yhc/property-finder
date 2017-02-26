package org.pyhc.propertyfinder.configuration;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class MongoExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        MongoTemplate mongoTemplate = testContext.getApplicationContext().getBean(MongoTemplate.class);
        mongoTemplate.getDb().dropDatabase();
    }
}
