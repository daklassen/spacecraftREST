package de.david.spacecraft.rest;

import de.david.spacecraft.persistence.Spacecraft;
import de.david.spacecraft.persistence.SpacecraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author David Klassen
 */
@RestController
@RequestMapping("/spacecrafts")
public class SpacecraftRestController {

    @Autowired
    SpacecraftRepository spacecraftRepository;

    @RequestMapping(method = RequestMethod.GET)
    Collection<Spacecraft> readSpacecrafts() {
        return spacecraftRepository.findAll();
    }
}
