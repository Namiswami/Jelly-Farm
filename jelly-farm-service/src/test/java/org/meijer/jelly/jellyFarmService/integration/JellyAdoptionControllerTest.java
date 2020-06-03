package org.meijer.jelly.jellyFarmService.integration;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.DataManager;
import org.meijer.jelly.jellyFarmService.JellyFarmServiceApplication;
import org.meijer.jelly.jellyFarmService.controller.JellyAdoptionController;
import org.meijer.jelly.jellyFarmService.exception.GlobalExceptionHandler;
import org.meijer.jelly.jellyFarmService.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.FreeJellyRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.RecageRequestDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.meijer.jelly.jellyFarmService.ObjectHelper.mapToJson;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.MALE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(
        classes = {JellyFarmServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@EmbeddedKafka(brokerProperties={"log.dir=./tmp/kafka/eventListenerTest", "port=9092", "listeners=PLAINTEXT://:9092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class JellyAdoptionControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private JellyAdoptionController adoptionController;

    @Autowired
    private JellyStockRepository jellyStockRepository;

    @Autowired
    private DataManager dataManager;

    @Before
    public void init() {
        mockMvc = standaloneSetup(adoptionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        dataManager.cleanUp();
    }

    @Test
    public void adoptionEndpointSavesNewJellyInTheDB() throws Exception {
        //given
        dataManager.createCage();

        AdoptionRequestDTO adoptionRequest = new AdoptionRequestDTO(1L, BLUE, MALE);

        //when
        mockMvc.perform(post("/v1/adoption/adopt")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(adoptionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cageNumber").value("1"))
                .andExpect(jsonPath("$.color").value(BLUE.toString()))
                .andExpect(jsonPath("$.gender").value(MALE.toString()))
                .andExpect(jsonPath("$.id").exists());

        //then
        List<JellyEntity> jellies = jellyStockRepository.findAll();
        assertEquals(1, jellies.size());

        JellyEntity jelly = jellies.get(0);
        assertEquals(Long.valueOf(1), jelly.getCageNumber());
        assertEquals(BLUE, jelly.getColor());
        assertEquals(MALE, jelly.getGender());
        assertNull(jelly.getDateTimeFreed());
        assertNotNull(jelly.getId());
    }

    @Test
    public void typeValidationFailureForJellyAdoptionRequestGivesBadRequest() throws Exception {
        JSONObject faultyInput = new JSONObject();
        faultyInput.put("color","not-a-color");
        faultyInput.put("gender","FEMALE");
        faultyInput.put("cageNumber","1");
        //when
        mockMvc.perform(post("/v1/adoption/adopt")
                .contentType(APPLICATION_JSON)
                .content(faultyInput.toString()))
                .andExpect(status().isBadRequest());

        faultyInput = new JSONObject();
        faultyInput.put("color","BLUE");
        faultyInput.put("gender","not-a-gender");
        faultyInput.put("cageNumber","1");
        //when
        mockMvc.perform(post("/v1/adoption/adopt")
                .contentType(APPLICATION_JSON)
                .content(faultyInput.toString()))
                .andExpect(status().isBadRequest());

        faultyInput = new JSONObject();
        faultyInput.put("color","BLUE");
        faultyInput.put("gender","MALE");
        faultyInput.put("cageNumber","not-a-long");
        //when
        mockMvc.perform(post("/v1/adoption/adopt")
                .contentType(APPLICATION_JSON)
                .content(faultyInput.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void adoptionReturnsBadRequestIfCageDoesntExist() throws Exception {
        AdoptionRequestDTO adoptionRequest = new AdoptionRequestDTO(1L, BLUE, MALE);

        //when
        mockMvc.perform(post("/v1/adoption/adopt")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(adoptionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void recageEndpointChangesCageOnJelly() throws Exception {
        //given
        UUID id = dataManager.saveNewJelly(1L).getId();
        dataManager.createCage(1L, "Grassy Field");
        dataManager.createCage(2L, "Humid Savannah");

        RecageRequestDTO recageRequest = new RecageRequestDTO(Collections.singletonList(id), 2L);

        //when
        mockMvc.perform(put("/v1/adoption/recage")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(recageRequest)))
                .andExpect(jsonPath("$.jellyList[0].cageNumber").value("2"))
                .andExpect(jsonPath("$.jellyList[0].color").value(BLUE.toString()))
                .andExpect(jsonPath("$.jellyList[0].gender").value(MALE.toString()))
                .andExpect(jsonPath("$.jellyList[0].id").exists());

        //then
        List<JellyEntity> jellies = jellyStockRepository.findAll();
        assertEquals(1, jellies.size());

        JellyEntity jelly = jellies.get(0);
        assertEquals(Long.valueOf(2), jelly.getCageNumber());
        assertEquals(BLUE, jelly.getColor());
        assertEquals(MALE, jelly.getGender());
        assertNull(jelly.getDateTimeFreed());
        assertNotNull(jelly.getId());
    }

    @Test
    public void recageEndpointGivesErrorMessageWhenCageDoesNotHaveSpace() throws Exception {
        //given
        UUID id = dataManager.saveNewJelly(1L).getId();
        dataManager.createCage(1L, "Grassy Field");
        dataManager.saveThreeBlueMales(20, 2L);
        dataManager.createCage(2L, "Humid Savannah");

        RecageRequestDTO recageRequest = new RecageRequestDTO(
                Collections.singletonList(id),
                2L);

        //when
        mockMvc.perform(put("/v1/adoption/recage")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(recageRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void recageEndpointGivesErrorMessageWhenJelliesAreAlreadyInTheNewCage() throws Exception {
        //given
        UUID id = dataManager.saveNewJelly(1L).getId();
        dataManager.createCage(1L, "Grassy Field");

        RecageRequestDTO recageRequest = new RecageRequestDTO(
                Collections.singletonList(id),
                1L);

        //when
        mockMvc.perform(put("/v1/adoption/recage")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(recageRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void freeEndpointGivesJellyATimeFreed() throws Exception {
        //given
        dataManager.createCage();
        UUID id = dataManager.saveNewJelly(1L).getId();
        FreeJellyRequestDTO freeRequest = new FreeJellyRequestDTO(Collections.singletonList(id));

        //when
        mockMvc.perform(delete("/v1/adoption/free")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(freeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jellyList[0].cageNumber").value("1"))
                .andExpect(jsonPath("$.jellyList[0].color").value(BLUE.toString()))
                .andExpect(jsonPath("$.jellyList[0].gender").value(MALE.toString()))
                .andExpect(jsonPath("$.jellyList[0].id").exists())
                .andExpect(jsonPath("$.jellyList[0].dateTimeFreed").exists());
    }
}
