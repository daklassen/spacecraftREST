package de.david.spacecraft.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author David Klassen
 */
public interface CaptainRepository extends JpaRepository<Captain, Long> {
}
