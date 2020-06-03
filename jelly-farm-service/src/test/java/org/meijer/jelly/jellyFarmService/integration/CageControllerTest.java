package org.meijer.jelly.jellyFarmService.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.DataManager;
import org.meijer.jelly.jellyFarmService.JellyFarmServiceApplication;
import org.meijer.jelly.jellyFarmService.controller.CageController;
import org.meijer.jelly.jellyFarmService.exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
public class CageControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private CageController cageController;


    @Autowired
    private DataManager dataManager;

    @Before
    public void init() {
            mockMvc = standaloneSetup(cageController)
                    .setControllerAdvice(new GlobalExceptionHandler())
                    .build();
            dataManager.cleanUp();
    }

    @Test
    public void allCagesEndpointReturnsListOfAllCages() throws Exception {
        //given
        dataManager.createCage();

        //when
        mockMvc.perform(get("/v1/cage/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cageList[0].cageNumber").value("1"))
                .andExpect(jsonPath("$.cageList[0].habitatName").value("Tropical Forest"));
    }
}
