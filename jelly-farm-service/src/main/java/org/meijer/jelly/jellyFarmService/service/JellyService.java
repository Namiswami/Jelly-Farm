package org.meijer.jelly.jellyFarmService.service;

import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.exception.JellyNotFoundException;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JellyService {
    private final JellyStockRepository jellyStockRepository;
    private final CageService cageService;
    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public JellyService(JellyStockRepository jellyStockRepository,
                        CageService cageService,
                        KafkaTemplate kafkaTemplate) {
        this.jellyStockRepository = jellyStockRepository;
        this.cageService = cageService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public JellyEntity getJelly(UUID id) {
        return jellyStockRepository.findById(id).get();
    }

    public JellyOverviewDTO getJellyOverview() {
        List<JellyEntity> allJellies = jellyStockRepository.findAll();
        return new JellyOverviewDTO(allJellies);
    }

    public List<CageOverviewDTO> getCageOverview() {
        List<CageOverviewDTO> cageOverviewDTOS = cageService.getOverview();
        for(CageOverviewDTO cageOverviewDTO : cageOverviewDTOS) {
            JellyOverviewDTO overview = new JellyOverviewDTO(
                    jellyStockRepository.findByCageNumberUnsold(cageOverviewDTO.getCage().getCageNumber()));
            cageOverviewDTO.setJellyOverview(overview);
        }
        return cageOverviewDTOS;
    }

    public List<CageOverviewDTO> getCageOverview(Long cageNumber) {
        return null;
    }

    public void buyNewJelly(Color color, Gender gender, long cageNumber) {
        if(!cageService.existsById(cageNumber)) {
            log.error("No cage with cage number {}", cageNumber);
            throw new CageNotFoundException();
        }

        JellyEntity jelly = JellyEntity.builder()
                .color(color)
                .cageNumber(cageNumber)
                .gender(gender)
                .dateTimeFreed(null)
                .build();

        jellyStockRepository.save(jelly);

    }

    public JellyDTO sellJelly(UUID id) {
        Optional<JellyEntity> jellyOptional = jellyStockRepository.findById(id);

        if(jellyOptional.isPresent()) {
            JellyEntity jelly = jellyOptional.get();
            jelly.setDateTimeFreed(LocalDateTime.now());
            jellyStockRepository.save(jelly);
            return new JellyDTO(jelly);
        } else throw new JellyNotFoundException();

    }

    public void deleteAll() {
        jellyStockRepository.deleteAll();
    }

    public List<JellyDTO> getJelliesByCage(Long cageNumber) {
        if(!cageService.existsById(cageNumber)) throw new CageNotFoundException();

        List<JellyEntity> entityList = jellyStockRepository.findByCageNumberUnsold(cageNumber);
        return entityList.stream()
                .map(JellyDTO::new)
                .collect(Collectors.toList());
    }

    public List<JellyDTO> getAllJellies() {
        List<JellyEntity> entityList = jellyStockRepository.findAll();
        return entityList.stream()
                .map(JellyDTO::new)
                .collect(Collectors.toList());
    }

    public void save(JellyDTO newBornJelly) {
        jellyStockRepository.save(new JellyEntity(newBornJelly));
    }
}
