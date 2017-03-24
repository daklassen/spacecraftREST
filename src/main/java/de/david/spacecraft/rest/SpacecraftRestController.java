package de.david.spacecraft.rest;

import de.david.spacecraft.persistence.Spacecraft;
import de.david.spacecraft.persistence.SpacecraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    Collection<Spacecraft> readAllSpacecrafts() {
        return spacecraftRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{spacecraftId}")
    Spacecraft readSpacecraftById(@PathVariable Long spacecraftId) {
        return spacecraftRepository.findOne(spacecraftId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody Spacecraft input) {

        Spacecraft result = spacecraftRepository.save(
                new Spacecraft(input.getIdentification(),
                        input.getCaptain(),
                        input.getComissioning(),
                        input.isAvailable(),
                        input.getType()));

        return createResposeEntityWithLocation(result);
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> update(@RequestBody Spacecraft input) {
        Spacecraft updatedSpacecraft = spacecraftRepository.findOne(input.getId());

        if (updatedSpacecraft == null) {
            return ResponseEntity.noContent().build();
        }

        updatedSpacecraft.setIdentification(input.getIdentification());
        updatedSpacecraft.setCaptain(input.getCaptain());
        updatedSpacecraft.setAvailable(input.isAvailable());
        updatedSpacecraft.setType(input.getType());
        Spacecraft result = spacecraftRepository.save(updatedSpacecraft);

        return createResposeEntityWithLocation(result);
    }

    private ResponseEntity createResposeEntityWithLocation(Spacecraft result) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
