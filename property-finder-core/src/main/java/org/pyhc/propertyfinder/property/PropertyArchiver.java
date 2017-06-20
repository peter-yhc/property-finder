package org.pyhc.propertyfinder.property;

import org.apache.log4j.Logger;
import org.pyhc.propertyfinder.events.ProfileResultEvent;
import org.pyhc.propertyfinder.property.model.SoldProperty;
import org.pyhc.propertyfinder.property.model.SoldPropertyRepository;
import org.pyhc.propertyfinder.scraper.realestate.result.SoldPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyArchiver {

    private static final Logger LOG = Logger.getLogger(PropertyArchiver.class);

    @Autowired
    private SoldPropertyRepository soldPropertyRepository;

    @Transactional
    @EventListener
    public void archiveSoldProperty(ProfileResultEvent profileResultEvent) {
        LOG.info("Archiving sold property");
        SoldPropertyProfile soldPropertyProfile = profileResultEvent.getSoldPropertyProfile();
        SoldProperty soldProperty = soldPropertyRepository.findOneByPropertyCode(soldPropertyProfile.getPropertyCode())
                .orElse(new SoldProperty(
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
                ));
        soldProperty.setPrice(soldPropertyProfile.getPrice());
        soldPropertyRepository.save(soldProperty);
    }

}
