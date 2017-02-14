package org.pyhc.propertyfinder.archive;

import org.pyhc.propertyfinder.scraper.realestate.query.PropertyProfile;

public interface PropertyArchiverPort {

    void archive(PropertyProfile propertyProfile);

}
