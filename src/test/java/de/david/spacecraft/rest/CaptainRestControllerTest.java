package de.david.spacecraft.rest;

import de.david.spacecraft.persistence.Captain;
import de.david.spacecraft.persistence.CaptainRepository;
import de.david.spacecraft.persistence.SpacecraftRepository;
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
public class CaptainRestControllerTest {
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

        mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat(mappingJackson2HttpMessageConverter).isNotNull();
    }

    private List<Captain> testCaptains;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        spacecraftRepository.deleteAllInBatch();
        captainRepository.deleteAllInBatch();

        testCaptains = new ArrayList<>();
        testCaptains.add(new Captain("James Tiberius1", "Kirk1"));
        testCaptains.add(new Captain("James Tiberius2", "Kirk2"));
        testCaptains.add(new Captain("James Tiberius3", "Kirk3"));
        testCaptains.add(new Captain("James Tiberius4", "Kirk4"));

        testCaptains.forEach(captain -> captainRepository.save(captain));
    }

    @Test
    public void readCaptains_WithSavedCaptains_ReturnsSavedCaptainsInValidJSON() throws Exception {
        mockMvc.perform(get("/captains"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].lastName", is("Kirk1")))
                .andExpect(jsonPath("$[1].lastName", is("Kirk2")))
                .andExpect(jsonPath("$[2].lastName", is("Kirk3")))
                .andExpect(jsonPath("$[3].lastName", is("Kirk4")));
    }

    @Test
    public void readSpacecrafts_WithZeroSpacecrafts_ReturnsEmptyJSON() throws Exception {
        this.captainRepository.deleteAllInBatch();

        mockMvc.perform(get("/captains"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
