package de.david.spacecraft.rest;

import de.david.spacecraft.persistence.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author David Klassen
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SpacecraftRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private SpacecraftRepository spacecraftRepository;

    @Autowired
    private CaptainRepository captainRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat(this.mappingJackson2HttpMessageConverter).isNotNull();
    }

    private List<Spacecraft> testSpacecrafts;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Captain captain = new Captain("James Tiberius", "Kirk");
        captainRepository.save(captain);

        testSpacecrafts = new ArrayList<>();
        testSpacecrafts.add(new Spacecraft("SA-23E Aurora", captain, new Date(), true, SpacecraftType.CRUISER));
        testSpacecrafts.add(new Spacecraft("Raider Fighter", captain, new Date(), true, SpacecraftType.FRIGATE));
        testSpacecrafts.add(new Spacecraft("Colonial Viper", captain, new Date(), true, SpacecraftType.FREIGHTER));
        testSpacecrafts.add(new Spacecraft("Star Fighter", captain, new Date(), false, SpacecraftType.FERRY));

        this.spacecraftRepository.deleteAllInBatch();

        testSpacecrafts.forEach(spacecraft -> this.spacecraftRepository.save(spacecraft));
    }

    @Test
    public void readSpacecrafts_WithSavedSpacecrafts_ReturnsSavedSpacecraftsInValidJSON() throws Exception {
        mockMvc.perform(get("/spacecrafts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].identification", is("SA-23E Aurora")))
                .andExpect(jsonPath("$[1].identification", is("Raider Fighter")))
                .andExpect(jsonPath("$[2].identification", is("Colonial Viper")))
                .andExpect(jsonPath("$[3].identification", is("Star Fighter")));
    }

    @Test
    public void readSpacecrafts_WithZeroSpacecrafts_ReturnsEmptyJSON() throws Exception {
        this.spacecraftRepository.deleteAllInBatch();

        mockMvc.perform(get("/spacecrafts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
