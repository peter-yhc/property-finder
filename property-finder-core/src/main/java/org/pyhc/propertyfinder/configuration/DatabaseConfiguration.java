package org.pyhc.propertyfinder.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
        "org.pyhc.propertyfinder.settings.model",
        "org.pyhc.propertyfinder.property.model"
})
public class DatabaseConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "property_finder_db";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost", 27017);
    }

    @Override
    protected String getMappingBasePackage() {
        return "org.pyhc.propertyfinder.property.model";
    }
}
