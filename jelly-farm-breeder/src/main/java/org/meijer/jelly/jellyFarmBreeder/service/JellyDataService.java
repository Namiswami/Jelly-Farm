package org.meijer.jelly.jellyFarmBreeder.service;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmBreeder.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.dto.JellyListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class JellyDataService {
    private final RestTemplate restTemplate;
    private final String jellyEndpoint;
    private final String cageEndpoint;

    @Autowired
    public JellyDataService(RestTemplate restTemplate,
                            @Value("${jelly.farm.jelly.endpoint}") String jellyEndpoint,
                            @Value("${jelly.farm.cage.endpoint}") String cageEndpoint) {
        this.restTemplate = restTemplate;
        this.jellyEndpoint = jellyEndpoint;
        this.cageEndpoint = cageEndpoint;
    }


    public List<CageDTO> getCages() {
        log.info("Retrieving cage data from jelly service");
        ResponseEntity<CageListDTO> response = restTemplate.getForEntity(cageEndpoint, CageListDTO.class);
        if(response.getBody() == null) {
            log.warn("No response body returned, continuing operations with empty list");
            return Collections.emptyList();
        }

        return response.getBody().getCageDTOList();
    }

    public List<JellyDTO> getUnsoldJellies(long cageNumber) {
        String uri = UriComponentsBuilder.fromHttpUrl(jellyEndpoint)
                .queryParam("cageNumber", cageNumber)
                .build()
                .toUriString();

        log.info("Retrieving jelly data from jelly service");
        ResponseEntity<JellyListDTO> response = restTemplate.getForEntity(uri, JellyListDTO.class);
        if(response.getBody() == null) {
            log.error("The call to the jelly service failed, no response body");
            return Collections.emptyList();
        }

        return response.getBody().getJellyDTOList();
    }
}
