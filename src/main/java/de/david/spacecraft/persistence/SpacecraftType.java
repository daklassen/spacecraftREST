package de.david.spacecraft.persistence;

/**
 * @author David Klassen
 */
public enum SpacecraftType {
    FREIGHTER("Freighter"),
    FRIGATE("Frigate"),
    CRUISER("Cruiser"),
    FERRY("Ferry");

    private String name;

    SpacecraftType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}