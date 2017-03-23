package de.david.spacecraft;

import de.david.spacecraft.persistence.Spacecraft;
import de.david.spacecraft.persistence.SpacecraftRepository;
import de.david.spacecraft.persistence.SpacecraftType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author David Klassen
 */
@SpringBootApplication
public class SpacecraftRestApplication implements CommandLineRunner {

	@Autowired
	SpacecraftRepository spacecraftRepository;

	@Override
	public void run(String... args) throws Exception {

		List<Spacecraft> spacecrafts = new ArrayList<>();
		spacecrafts.add(new Spacecraft("SA-23E Aurora", "James Tiberius Kirk", new Date(), true, SpacecraftType.CRUISER));
		spacecrafts.add(new Spacecraft("Raider Fighter", "Colonel Jack O'Neil", new Date(), true, SpacecraftType.FRIGATE));
		spacecrafts.add(new Spacecraft("Colonial Viper", "David Bowman", new Date(), true, SpacecraftType.FREIGHTER));
		spacecrafts.add(new Spacecraft("Star Fighter", "William T. Riker", new Date(), false, SpacecraftType.FERRY));

		spacecrafts.forEach(spacecraft -> spacecraftRepository.save(spacecraft));
	}

	public static void main(String[] args) {
		SpringApplication.run(SpacecraftRestApplication.class, args);
	}
}
