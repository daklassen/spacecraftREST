package de.david.spacecraft;

import de.david.spacecraft.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    CaptainRepository captainRepository;

    @Override
    public void run(String... args) throws Exception {

        Captain captain = captainRepository.save(new Captain("James Tiberius", "Kirk"));

        List<Spacecraft> spacecrafts = new ArrayList<>();
        spacecrafts.add(new Spacecraft("SA-23E Aurora", captain, new Date(), true, SpacecraftType.CRUISER));
        spacecrafts.add(new Spacecraft("Raider Fighter", captain, new Date(), true, SpacecraftType.FRIGATE));
        spacecrafts.add(new Spacecraft("Colonial Viper", captain, new Date(), true, SpacecraftType.FREIGHTER));
        spacecrafts.add(new Spacecraft("Star Fighter", captain, new Date(), false, SpacecraftType.FERRY));

        spacecrafts.forEach(spacecraft -> spacecraftRepository.save(spacecraft));
    }

    public static void main(String[] args) {
        SpringApplication.run(SpacecraftRestApplication.class, args);
    }
}
