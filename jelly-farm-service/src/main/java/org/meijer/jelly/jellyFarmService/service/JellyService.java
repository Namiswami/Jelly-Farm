package org.meijer.jelly.jellyFarmService.service;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JellyService {
    private final JellyStockRepository jellyStockRepository;
    private final CageService cageService;

    @Autowired
    public JellyService(JellyStockRepository jellyStockRepository,
                        CageService cageService) {
        this.jellyStockRepository = jellyStockRepository;
        this.cageService = cageService;
    }

    public JellyDTO getJelly(UUID id) {
        return new JellyDTO(jellyStockRepository.getOne(id));
    }

    public JellyOverviewDTO getJellyOverview() {
        return new JellyOverviewDTO(jellyStockRepository.findAll());
    }

    public List<CageOverviewDTO> getCageOverview() {
        return createOverview(cageService.getCageOverview());
    }

    public List<CageOverviewDTO> getCageOverview(List<Long> cageNumbers) {
        return createOverview(cageService.getCageOverview(cageNumbers));
    }

    private List<CageOverviewDTO> createOverview(List<CageOverviewDTO> cageOverviewDTOS) {
        for(CageOverviewDTO cage : cageOverviewDTOS) {
            JellyOverviewDTO overview = new JellyOverviewDTO(
                    jellyStockRepository.findByCageNumberUnsold(cage.getCage().getCageNumber()));
            cage.setJellyOverview(overview);
        }
        return cageOverviewDTOS;
    }

    public List<JellyDTO> getJellies(Long cageNumber, Color color, Gender gender) {
        if(cageNumber != null && !cageService.existsById(cageNumber)) {
            log.error("Cage with cage number: {} does not exist", cageNumber);
            throw new CageNotFoundException();
        }

        Example<JellyEntity> example = getJellyEntityExample(cageNumber, color, gender);
        return jellyStockRepository.findAll(example).stream()
                .map(JellyDTO::new)
                .collect(Collectors.toList());
    }

    private Example<JellyEntity> getJellyEntityExample(Long cageNumber, Color color, Gender gender) {
        JellyEntity exampleEntity = JellyEntity.builder()
                .cageNumber(cageNumber)
                .color(color)
                .gender(gender)
                .build();
        return Example.of(exampleEntity);
    }
}
