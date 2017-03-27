package de.david.spacecraft.restcontroller;

import de.david.spacecraft.persistence.Captain;
import de.david.spacecraft.persistence.CaptainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author David Klassen
 */
@RestController
@RequestMapping("/captains")
public class CaptainRestController {

    @Autowired
    CaptainRepository captainRepository;

    @RequestMapping(method = RequestMethod.GET)
    Collection<Captain> readAllCaptains() {
        return captainRepository.findAll();
    }
}
