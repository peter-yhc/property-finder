package org.pyhc.propertyfinder.property;

import org.apache.log4j.Logger;
import org.pyhc.propertyfinder.property.model.SoldProperty;
import org.pyhc.propertyfinder.property.model.SoldPropertyRepository;
import org.pyhc.propertyfinder.scraper.realestate.result.PropertyProfile;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyArchiver implements PropertyArchiverPort {

    private static final Logger LOG = Logger.getLogger(PropertyArchiver.class);

    @Autowired
    private SoldPropertyRepository soldPropertyRepository;

    @Override
    public void archiveListedProperty(PropertyProfile propertyProfile) {
        throw new UnsupportedOperationException();

    }

    @Transactional
    public void archiveSoldProperty(SoldPropertyProfile newPropertyProfile) {
        LOG.info("Archiving sold property");
        SoldProperty soldProperty = soldPropertyRepository.findOneByPropertyCode(newPropertyProfile.getPropertyCode())
                .orElse(new SoldProperty(
                        newPropertyProfile.getPrice(),
                        newPropertyProfile.getAddress(),
                        newPropertyProfile.getBed(),
                        newPropertyProfile.getBath(),
                        newPropertyProfile.getCar(),
                        newPropertyProfile.getSuburb(),
                        newPropertyProfile.getPostcode(),
                        newPropertyProfile.getPropertyCode(),
                        newPropertyProfile.getPropertyLink(),
                        newPropertyProfile.getSoldDate()
                ));
        soldProperty.setPrice(newPropertyProfile.getPrice());
        soldPropertyRepository.save(soldProperty);
    }

}
