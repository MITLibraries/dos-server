package edu.mit.dos.object;

/**
 * Generates unique ids. There can be different implementations for this to implement
 * the strategy to generate identifiers.
 */
public interface Minter {

    String generate();

}
