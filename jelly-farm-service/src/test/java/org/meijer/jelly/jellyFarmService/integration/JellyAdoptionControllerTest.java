package org.meijer.jelly.jellyFarmService.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmService.DataManager;
import org.meijer.jelly.jellyFarmService.JellyFarmServiceApplication;
import org.meijer.jelly.jellyFarmService.controller.CageController;
import org.meijer.jelly.jellyFarmService.controller.JellyAdoptionController;
import org.meijer.jelly.jellyFarmService.exception.GlobalExceptionHandler;
import org.meijer.jelly.jellyFarmService.integration.config.KafkaTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(
        classes = {JellyFarmServiceApplication.class, KafkaTestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@Import(KafkaTestConfiguration.class)
@EmbeddedKafka(brokerProperties={"log.dir=./tmp/kafka/eventListenerTest", "port=9092", "listeners=PLAINTEXT://:9092"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JellyAdoptionControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private JellyAdoptionController adoptionController;


    @Autowired
    private DataManager dataManager;

    @Before
    public void init() {
        mockMvc = standaloneSetup(adoptionController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void allCagesEndpointReturnsListOfAllCages() throws Exception {
        //given
        dataManager.createDefaultCage();

        //when
        mockMvc.perform(get("/v1/adoption/")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cageList[0].cageNumber").value("1"))
                .andExpect(jsonPath("$.cageList[0].habitatName").value("Tropical Forest"));
    }
}
