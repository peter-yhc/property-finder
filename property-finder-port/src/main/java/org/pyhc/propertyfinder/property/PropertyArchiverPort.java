package org.pyhc.propertyfinder.property;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;

public interface PropertyArchiverPort {

    void archiveListedProperty(PropertyProfile propertyProfile);

    void archiveSoldProperty(SoldPropertyProfile soldPropertyProfile);
}
