package edu.mit.dos.object;

import edu.mit.dos.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class IdentifierFactory {

    private static final Logger logger = LoggerFactory.getLogger(IdentifierFactory.class);

    @Autowired
    @Qualifier("UUIDMinter")
    private UUIDMinter UUIDMinter;

    @Autowired
    private ServiceConfig serviceConfig;

    /**
     * Return based on whatever we want
     * @return
     */
    public Minter getInstance() {

        if (serviceConfig == null) {
            logger.error("Couldn't inject properties!!");
        }

        if (serviceConfig.getMinter().equalsIgnoreCase("pid")) {
            logger.debug("Returning pid minter");
            return UUIDMinter;
        } else {
            logger.debug("Returning default minter");
        }

        return UUIDMinter;
    }
}
