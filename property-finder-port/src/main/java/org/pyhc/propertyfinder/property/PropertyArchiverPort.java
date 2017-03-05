package org.pyhc.propertyfinder.property;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;

public interface PropertyArchiverPort {

    void archive(PropertyProfile propertyProfile);

}
