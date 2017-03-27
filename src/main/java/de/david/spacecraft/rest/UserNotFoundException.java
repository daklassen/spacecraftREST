package de.david.spacecraft.rest;

/**
 * @author David Klassen
 */
class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("could not find user '" + userId + "'.");
    }
}
