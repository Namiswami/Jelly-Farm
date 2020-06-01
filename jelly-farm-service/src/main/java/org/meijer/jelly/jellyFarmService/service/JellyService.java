package org.meijer.jelly.jellyFarmService.service;

import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.exception.JellyNotFoundException;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewListDTO;
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
        List<CageOverviewDTO> cageOverviewDTOS = cageService.getCageOverview();
        return createOverview(cageOverviewDTOS);
    }

    public List<CageOverviewDTO> getCageOverview(List<Long> cageNumbers) {
        List<CageOverviewDTO> cageOverviewDTOS = cageService.getCageOverview(cageNumbers);
        return createOverview(cageOverviewDTOS);
    }

    private List<CageOverviewDTO> createOverview(List<CageOverviewDTO> cageOverviewDTOS) {
        for(CageOverviewDTO cage : cageOverviewDTOS) {
            JellyOverviewDTO overview = new JellyOverviewDTO(
                    jellyStockRepository.findByCageNumberUnsold(cage.getCage().getCageNumber()));
            cage.setJellyOverview(overview);
        }
        return cageOverviewDTOS;
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
