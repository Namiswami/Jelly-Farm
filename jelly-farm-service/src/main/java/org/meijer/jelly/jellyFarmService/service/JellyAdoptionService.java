package org.meijer.jelly.jellyFarmService.service;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JellyAdoptionService {
    private final JellyStockRepository jellyStockRepository;

    @Autowired
    public JellyAdoptionService(JellyStockRepository jellyStockRepository) {
        this.jellyStockRepository = jellyStockRepository;
    }

    public void save(JellyDTO newBornJelly) {
        log.info("A new {} {} jelly was welcomed into cage {}",
                newBornJelly.getGender(),
                newBornJelly.getColor(),
                newBornJelly.getCageNumber());
        jellyStockRepository.save(new JellyEntity(newBornJelly));
    }
}
