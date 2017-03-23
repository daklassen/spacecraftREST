package de.david.spacecraft.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author David Klassen
 */
public interface SpacecraftRepository extends JpaRepository<Spacecraft, Long> {
    Optional<Spacecraft> findByIdentification(String identification);
}
