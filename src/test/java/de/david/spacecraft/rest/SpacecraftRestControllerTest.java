package de.david.spacecraft.rest;

import de.david.spacecraft.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

        mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat(mappingJackson2HttpMessageConverter).isNotNull();
    }

    private List<Spacecraft> testSpacecrafts;
    private Captain testCaptain;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        spacecraftRepository.deleteAllInBatch();
        captainRepository.deleteAllInBatch();

        testCaptain = new Captain("James Tiberius", "Kirk");
        captainRepository.save(testCaptain);

        testSpacecrafts = new ArrayList<>();
        testSpacecrafts.add(new Spacecraft("SA-23E Aurora", testCaptain, new Date(), true, SpacecraftType.CRUISER));
        testSpacecrafts.add(new Spacecraft("Raider Fighter", testCaptain, new Date(), true, SpacecraftType.FRIGATE));
        testSpacecrafts.add(new Spacecraft("Colonial Viper", testCaptain, new Date(), true, SpacecraftType.FREIGHTER));
        testSpacecrafts.add(new Spacecraft("Star Fighter", testCaptain, new Date(), false, SpacecraftType.FERRY));

        testSpacecrafts.forEach(spacecraft -> spacecraftRepository.save(spacecraft));
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

    @Test
    public void addSpacecrafts_WithWrongSyntax_ReturnsBadRequest() throws Exception {
        this.spacecraftRepository.deleteAllInBatch();

        mockMvc.perform(post("/spacecrafts")
                .content("{wrong:syntax}")
                .contentType(contentType))
                .andExpect(status().is(400)); // HTTP status 400 : "Bad Request"
    }

    @Test
    public void addSpacecrafts_WithValidSpacecraft_ReturnsIsCreated() throws Exception {
        this.spacecraftRepository.deleteAllInBatch();

        Spacecraft spacecraft = new Spacecraft("SA-23E Aurora", testCaptain, new Date(), true, SpacecraftType.CRUISER);

        mockMvc.perform(post("/spacecrafts")
                .content(json(spacecraft))
                .contentType(contentType))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateSpacecraft_WithExistingSpacecraft_UpdatesSpacecraft() throws Exception {
        spacecraftRepository.deleteAllInBatch();

        Spacecraft spacecraft = new Spacecraft("SA-23E Aurora", testCaptain, new Date(), true, SpacecraftType.CRUISER);
        Spacecraft savedSpacecraft = spacecraftRepository.save(spacecraft);

        String newName = "New SA-23E Aurora";
        savedSpacecraft.setIdentification(newName);

        mockMvc.perform(put("/spacecrafts")
                .content(json(savedSpacecraft))
                .contentType(contentType))
                .andExpect(status().isCreated());

        assertThat(spacecraftRepository.findOne(savedSpacecraft.getId()).getIdentification())
                .isEqualTo(newName);

    }

    @Test
    public void updateSpacecraft_WithNonExistingSpacecraft_ReturnsNoContent() throws Exception {
        spacecraftRepository.deleteAllInBatch();

        Spacecraft spacecraft = new Spacecraft("SA-23E Aurora", testCaptain, new Date(), true, SpacecraftType.CRUISER);

        JSONObject jsonObject = new JSONObject(json(spacecraft));
        jsonObject.put("id", 5);

        mockMvc.perform(put("/spacecrafts")
                .content(jsonObject.toString())
                .contentType(contentType))
                .andExpect(status().is(204)); // HTTP status 204 : "No Content"
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
