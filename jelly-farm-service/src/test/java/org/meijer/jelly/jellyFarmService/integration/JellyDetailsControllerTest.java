package org.meijer.jelly.jellyFarmService.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.DataManager;
import org.meijer.jelly.jellyFarmService.JellyFarmServiceApplication;
import org.meijer.jelly.jellyFarmService.controller.JellyDetailsController;
import org.meijer.jelly.jellyFarmService.exception.GlobalExceptionHandler;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.*;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.FEMALE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.MALE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(
        classes = {JellyFarmServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@EmbeddedKafka(brokerProperties={"log.dir=./tmp/kafka/eventListenerTest", "port=9092", "listeners=PLAINTEXT://:9092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class JellyDetailsControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private JellyDetailsController jellyDetailsController;

    @Autowired
    private JellyStockRepository jellyStockRepository;

    @Autowired
    private DataManager dataManager;

    @Before
    public void init() {
        mockMvc = standaloneSetup(jellyDetailsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        dataManager.cleanUp();
        dataManager.createCage();
    }

    @Test
    public void getAllJelliesReturnsAllJellies() throws Exception {
        //given
        dataManager.saveThreeBlueMales(3, 1L);

        //when-then
        mockMvc.perform(get("/v1/details/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jellyList", hasSize(3)))
                .andExpect(jsonPath("$.jellyList.[*].color", containsInAnyOrder(BLUE.toString(), BLUE.toString(), BLUE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].cageNumber", containsInAnyOrder(1,1,1)))
                .andExpect(jsonPath("$.jellyList.[*].gender", containsInAnyOrder(MALE.toString(), MALE.toString() ,MALE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].id", hasSize(3)));
    }

    @Test
    public void getAllJelliesReturnsAllJelliesFilteredByColor() throws Exception {
        //given
        dataManager.saveThreeBlueMales(3, 1L);
        dataManager.saveNewJelly(MALE, YELLOW);

        //when-then
        mockMvc.perform(get("/v1/details/stock")
                .param("color", BLUE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jellyList", hasSize(3)))
                .andExpect(jsonPath("$.jellyList.[*].color", containsInAnyOrder(BLUE.toString(), BLUE.toString(), BLUE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].cageNumber", containsInAnyOrder(1,1,1)))
                .andExpect(jsonPath("$.jellyList.[*].gender", containsInAnyOrder(MALE.toString(), MALE.toString() ,MALE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].id", hasSize(3)));
    }

    @Test
    public void getAllJelliesReturnsAllJelliesFilterByGenderAndColor() throws Exception {
        //given
        dataManager.saveThreeBlueMales(3, 1L);
        dataManager.saveNewJelly(FEMALE, BLUE);
        dataManager.saveNewJelly(MALE, RED);

        //when-then
        mockMvc.perform(get("/v1/details/stock")
                .param("color", BLUE.toString())
                .param("gender", MALE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jellyList", hasSize(3)))
                .andExpect(jsonPath("$.jellyList.[*].color", containsInAnyOrder(BLUE.toString(), BLUE.toString(), BLUE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].cageNumber", containsInAnyOrder(1,1,1)))
                .andExpect(jsonPath("$.jellyList.[*].gender", containsInAnyOrder(MALE.toString(), MALE.toString() ,MALE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].id", hasSize(3)));
    }

    @Test
    public void getAllJelliesReturnsAllJelliesFilteredByCageGenderAndColor() throws Exception {
        //given
        dataManager.saveThreeBlueMales(3, 1L);
        dataManager.saveNewJelly(2L);
        dataManager.saveNewJelly(MALE, RED);
        dataManager.saveNewJelly(FEMALE, BLUE);

        //when-then
        mockMvc.perform(get("/v1/details/stock")
                .param("color", BLUE.toString())
                .param("cageNumber", "1")
                .param("gender", MALE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jellyList", hasSize(3)))
                .andExpect(jsonPath("$.jellyList.[*].color", containsInAnyOrder(BLUE.toString(), BLUE.toString(), BLUE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].cageNumber", containsInAnyOrder(1,1,1)))
                .andExpect(jsonPath("$.jellyList.[*].gender", containsInAnyOrder(MALE.toString(), MALE.toString() ,MALE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].id", hasSize(3)));
    }

    @Test
    public void getAllJelliesReturnsAllJelliesExceptFreedJellies() throws Exception {
        //given
        dataManager.saveFreedJelly();
        dataManager.saveThreeBlueMales(3, 1L);

        //when-then
        mockMvc.perform(get("/v1/details/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jellyList", hasSize(3)))
                .andExpect(jsonPath("$.jellyList.[*].color", containsInAnyOrder(BLUE.toString(), BLUE.toString(), BLUE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].cageNumber", containsInAnyOrder(1,1,1)))
                .andExpect(jsonPath("$.jellyList.[*].gender", containsInAnyOrder(MALE.toString(), MALE.toString() ,MALE.toString())))
                .andExpect(jsonPath("$.jellyList.[*].id", hasSize(3)));
    }

    @Test
    public void getAllJelliesGives400WhenInvalidParameterValues() throws Exception {
        mockMvc.perform(get("/v1/details/stock")
                .param("gender", "not-a-gender"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getSingleJellyReturnsSingleJelly() throws Exception {
        //given
        JellyEntity jelly = dataManager.saveNewJelly(1L);

        //when-then
        mockMvc.perform(get("/v1/details/stock/" + jelly.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value(BLUE.toString()))
                .andExpect(jsonPath("$.cageNumber").value(1))
                .andExpect(jsonPath("$.gender").value(MALE.toString()))
                .andExpect(jsonPath("$.id").value(jelly.getId().toString()));
    }

    @Test
    public void getSingleJellyReturns404WhenNotFound() throws Exception {
        //when-then
        mockMvc.perform(get("/v1/details/stock/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

}
