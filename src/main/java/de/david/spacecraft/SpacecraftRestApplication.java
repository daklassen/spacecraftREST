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

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {

        Account account = new Account("username", "password");
        accountRepository.save(account);

        Captain captain = captainRepository.save(new Captain("James Tiberius", "Kirk"));

        List<Spacecraft> spacecrafts = new ArrayList<>();
        spacecrafts.add(new Spacecraft("SA-23E Aurora", captain, new Date(), true, SpacecraftType.CRUISER));
        spacecrafts.add(new Spacecraft("Raider Fighter", captain, new Date(), true, SpacecraftType.FRIGATE));
        spacecrafts.add(new Spacecraft("Colonial Viper", captain, new Date(), true, SpacecraftType.FREIGHTER));
        spacecrafts.add(new Spacecraft("Star Fighter", captain, new Date(), false, SpacecraftType.FERRY));

        spacecrafts.forEach(spacecraft -> spacecraftRepository.save(spacecraft));
    }

    // CORS
    @Bean
    FilterRegistrationBean corsFilter(
            @Value("${tagit.origin:http://localhost:9000}") String origin) {
        return new FilterRegistrationBean(new Filter() {
            public void doFilter(ServletRequest req, ServletResponse res,
                                 FilterChain chain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) res;
                String method = request.getMethod();
                // this origin value could just as easily have come from a database
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Methods",
                        "POST,GET,OPTIONS,DELETE");
                response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader(
                        "Access-Control-Allow-Headers",
                        "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
                if ("OPTIONS".equals(method)) {
                    response.setStatus(HttpStatus.OK.value());
                }
                else {
                    chain.doFilter(req, res);
                }
            }

            public void init(FilterConfig filterConfig) {
            }

            public void destroy() {
            }
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(SpacecraftRestApplication.class, args);
    }
}
