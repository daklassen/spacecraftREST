package de.david.spacecraft.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * @author David Klassen
 */
public interface SpacecraftRepository extends JpaRepository<Spacecraft, Long> {
    Collection<Spacecraft> findByAvailableTrue();

    Collection<Spacecraft> findByType(SpacecraftType type);
}
