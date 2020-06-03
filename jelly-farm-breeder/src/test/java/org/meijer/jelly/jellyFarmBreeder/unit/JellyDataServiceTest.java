package org.meijer.jelly.jellyFarmBreeder.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyListDTO;
import org.meijer.jelly.jellyFarmBreeder.service.JellyDataService;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmBreeder.model.jelly.attributes.Gender.FEMALE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JellyDataServiceTest {
    @Mock
    private RestTemplate restTemplate;

    private JellyDataService jellyDataService;

    @Before
    public void init() {
        String fakeEndpoint = "http://localhost:8081/v1/mock";
        jellyDataService = new JellyDataService(restTemplate, fakeEndpoint, fakeEndpoint);
    }

    @Test
    public void getCagesReturnsListOfCages() {
        //given
        List<CageDTO> cageDTOList = new ArrayList<>();
        cageDTOList.add(new CageDTO(1, "Indoor Hotsprings"));
        CageListDTO response = new CageListDTO();
        response.setCageDTOList(cageDTOList);

        when(restTemplate.getForEntity(anyString(), eq(CageListDTO.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //when
        List<CageDTO> results = jellyDataService.getCages();

        //then
        assertFalse(results.isEmpty());
        CageDTO result = results.get(0);
        assertEquals(1, result.getCageNumber());
        assertEquals("Indoor Hotsprings", result.getHabitatName());
    }

    @Test
    public void getCagesReturnsEmptyListIfHttpResponseBodyIsNull() {
        //given
        when(restTemplate.getForEntity(anyString(), eq(CageListDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        //when
        List<CageDTO> results = jellyDataService.getCages();

        //then
        assertTrue(results.isEmpty());
    }

    @Test
    public void getJelliesReturnsListOfCages() {
        //given
        List<JellyDTO> jellyDTOs = new ArrayList<>();
        JellyDTO jelly = JellyDTO.builder()
                .cageNumber(1)
                .color(BLUE)
                .dateTimeFreed(null)
                .gender(FEMALE)
                .id(null)
                .build();
        jellyDTOs.add(jelly);
        JellyListDTO response = new JellyListDTO(jellyDTOs);

        when(restTemplate.getForEntity(anyString(), eq(JellyListDTO.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //when
        List<JellyDTO> results = jellyDataService.getUnsoldJellies(1);

        //then
        assertFalse(results.isEmpty());
        JellyDTO result = results.get(0);
        assertEquals(1, result.getCageNumber());
        assertEquals(BLUE, result.getColor());
        assertEquals(FEMALE, result.getGender());
        assertNull(result.getId());
        assertNull(result.getDateTimeFreed());
    }

    @Test
    public void getJelliesReturnsEmptyListIfHttpResponseBodyIsNull() {
        //given
        when(restTemplate.getForEntity(anyString(), eq(JellyListDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        //when
        List<JellyDTO> results = jellyDataService.getUnsoldJellies(1);

        //then
        assertTrue(results.isEmpty());
    }

}
