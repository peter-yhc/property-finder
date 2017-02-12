package org.pyhc.propertyfinder.archive;

import org.pyhc.propertyfinder.scraper.model.PropertyProfile;

public interface PropertyArchiverPort {

    void archive(PropertyProfile propertyProfile);

}
