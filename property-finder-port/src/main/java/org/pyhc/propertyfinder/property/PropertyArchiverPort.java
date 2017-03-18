package org.pyhc.propertyfinder.property;

import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;

public interface PropertyArchiverPort {

    void archive(PropertyProfile propertyProfile);

    void archiveSoldProperty(SoldPropertyProfile soldPropertyProfile);
}
