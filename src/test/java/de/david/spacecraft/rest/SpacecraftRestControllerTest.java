package de.david.spacecraft.rest;

import de.david.spacecraft.persistence.Spacecraft;
import de.david.spacecraft.persistence.SpacecraftRepository;
import de.david.spacecraft.persistence.SpacecraftType;
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
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private WebApplicationContext context;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat(this.mappingJackson2HttpMessageConverter).isNotNull();
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        this.spacecraftRepository.deleteAllInBatch();
        this.spacecraftRepository.save(
                new Spacecraft("SA-23E Aurora", "James Tiberius Kirk", new Date(), true, SpacecraftType.CRUISER)
        );
    }

    @Test
    public void readSpacecrafts_WithOneSavedSpacecraft_ReturnsSavedSpacecraftInValidJSON() throws Exception {
        mockMvc.perform(get("/spacecrafts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].identification", is("SA-23E Aurora")));
    }
}
