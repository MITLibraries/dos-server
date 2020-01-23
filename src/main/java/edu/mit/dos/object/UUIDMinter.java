package edu.mit.dos.object;

import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Simple UUID based minting.
 */
@Service
public class UUIDMinter implements Minter {

    @Override
    public String generate() {
        final UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
