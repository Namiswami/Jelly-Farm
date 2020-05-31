package org.meijer.jelly.jellyFarmBreeder.service;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmBreeder.model.cage.CageDTO;
import org.meijer.jelly.jellyFarmBreeder.model.cage.CageListDTO;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.Jelly;
import org.meijer.jelly.jellyFarmBreeder.model.jelly.JellyListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ResponseEntity<CageListDTO> response = restTemplate.getForEntity(cageEndpoint, CageListDTO.class);

        if(response.getBody() == null) new ArrayList<>();
        return response.getBody().getCageDTOList();
    }

    public List<Jelly> getUnsoldJellies(long cageNumber) {
        Map<String, Long> params = new HashMap<>();
        params.put("cageNumber", cageNumber);

        ResponseEntity<JellyListDTO> response = restTemplate.getForEntity(jellyEndpoint, JellyListDTO.class, params);

        if(response.getBody() == null) throw new RestClientException("");
        return response.getBody().getJellyList();
    }
}
