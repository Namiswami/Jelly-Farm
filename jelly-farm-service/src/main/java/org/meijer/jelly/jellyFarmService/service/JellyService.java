package org.meijer.jelly.jellyFarmService.service;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.exception.NotEnoughRoomInCageException;
import org.meijer.jelly.jellyFarmService.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.FreeJellyRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.RecageRequestDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JellyService {
    private final JellyStockRepository jellyStockRepository;
    private final CageService cageService;
    private final Integer cageLimit;

    @Autowired
    public JellyService(JellyStockRepository jellyStockRepository,
                        CageService cageService,
                        @Value("${cage.limit}") Integer cageLimit) {
        this.jellyStockRepository = jellyStockRepository;
        this.cageService = cageService;
        this.cageLimit = cageLimit;
    }

    public JellyDTO getJelly(UUID id) {
        return new JellyDTO(jellyStockRepository.getOne(id));
    }

    public JellyOverviewDTO getJellyOverview() {
        return new JellyOverviewDTO(jellyStockRepository.findAll());
    }

    public List<CageOverviewDTO> getCageOverview() {
        List<CageOverviewDTO> results = new ArrayList<>();
        for(CageOverviewDTO cage : cageService.getCageOverview()) {
            results.add(createOverview(cage));
        }
        return results;
    }

    public CageOverviewDTO getCageOverview(Long cageNumber) {
        return createOverview(cageService.getCageOverview(cageNumber));
    }

    private CageOverviewDTO createOverview(CageOverviewDTO cage) {
        JellyOverviewDTO overview = new JellyOverviewDTO(
                jellyStockRepository.findByCageNumberUnsold(cage.getCage().getCageNumber()));
        cage.setJellyOverview(overview);
        return cage;
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

    public JellyDTO adoptJelly(AdoptionRequestDTO adoptionRequest) {
        log.info("A new {} {} jelly has been adopted in cage {}",
                adoptionRequest.getGender(),
                adoptionRequest.getColor(),
                adoptionRequest.getCageNumber());
        JellyEntity newJelly = JellyEntity.builder()
                .cageNumber(adoptionRequest.getCageNumber())
                .color(adoptionRequest.getColor())
                .gender(adoptionRequest.getGender())
                .build();
        return new JellyDTO(jellyStockRepository.save(newJelly));
    }

    public List<JellyDTO> recageJellies(RecageRequestDTO recageRequestDTO) {
        log.info("Checking if there is enough room in cage {}", recageRequestDTO.getNewCageNumber());
        if(!isEnoughRoomInCage(recageRequestDTO.getNewCageNumber(), recageRequestDTO.getJellyIds().size()))
            throw new NotEnoughRoomInCageException(recageRequestDTO.getNewCageNumber());

        log.info("Retrieving all jellies to be updated");
        List<JellyEntity> entities = jellyStockRepository.findAllById(recageRequestDTO.getJellyIds());
        for(JellyEntity entity : entities) {
            entity.setCageNumber(recageRequestDTO.getNewCageNumber());
        }

        log.info("Updating all jellies with new cage");
        return jellyStockRepository.saveAll(entities).stream()
                .map(JellyDTO::new)
                .collect(Collectors.toList());
    }

    public List<JellyDTO> freeJellies(FreeJellyRequestDTO freeJellyRequestDTO) {
        log.info("Retrieving all jellies to be freed");
        List<JellyEntity> entities = jellyStockRepository.findAllById(freeJellyRequestDTO.getJellyIds());

        LocalDateTime now = LocalDateTime.now();
        for(JellyEntity entity : entities) {
            entity.setDateTimeFreed(now);
        }

        log.info("Freeing all jellies in the list");
        return jellyStockRepository.saveAll(entities).stream()
                .map(JellyDTO::new)
                .collect(Collectors.toList());
    }

    private boolean isEnoughRoomInCage(Long newCageNumber, int numberOfNewJellies) {
        CageOverviewDTO overview = cageService.getCageOverview(newCageNumber);
        return (overview.getJellyOverview().getTotal() + numberOfNewJellies) <= cageLimit;
    }
}
