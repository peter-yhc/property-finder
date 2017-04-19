package org.pyhc.propertyfinder.property;

import org.apache.log4j.Logger;
import org.pyhc.propertyfinder.property.model.SoldProperty;
import org.pyhc.propertyfinder.property.model.SoldPropertyRepository;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyArchiver implements PropertyArchiverPort {

    private static final Logger LOG = Logger.getLogger(PropertyArchiver.class);

    @Autowired
    private SoldPropertyRepository soldPropertyRepository;

    @Override
    public void archiveListedProperty(PropertyProfile propertyProfile) {
        throw new UnsupportedOperationException();

    }

    public void archiveSoldProperty(SoldPropertyProfile soldPropertyProfile) {
        LOG.info("Archiving sold property");
        Optional<SoldProperty> savedProfileOptional = soldPropertyRepository.findOneByPropertyCode(soldPropertyProfile.getPropertyCode());
        if (!savedProfileOptional.isPresent()) {
            SoldProperty soldProperty = new SoldProperty(
                    soldPropertyProfile.getPrice(),
                    soldPropertyProfile.getAddress(),
                    soldPropertyProfile.getBed(),
                    soldPropertyProfile.getBath(),
                    soldPropertyProfile.getCar(),
                    soldPropertyProfile.getSuburb(),
                    soldPropertyProfile.getPostcode(),
                    soldPropertyProfile.getPropertyCode(),
                    soldPropertyProfile.getPropertyLink(),
                    soldPropertyProfile.getSoldDate()
            );
            soldPropertyRepository.save(soldProperty);
        }
    }

}
