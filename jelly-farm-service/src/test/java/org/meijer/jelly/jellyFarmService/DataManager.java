package org.meijer.jelly.jellyFarmService;

import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.repository.CageRepository;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color.BLUE;
import static org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender.*;

@Service
public class DataManager {
    private final JellyStockRepository jellyStockRepository;
    private final CageRepository cageRepository;

    @Autowired
    public DataManager(JellyStockRepository jellyStockRepository, CageRepository cageRepository) {
        this.jellyStockRepository = jellyStockRepository;
        this.cageRepository = cageRepository;
    }

    public void createCage() {
        createCage(1L);
    }

    public void createCage(Long cageNumber) {
        createCage(cageNumber, "Tropical Forest");
    }

    public void createCage(Long cageNumber, String habitatName) {
        cageRepository.save(new CageEntity(cageNumber, habitatName));
    }


    public JellyEntity saveNewJelly(Gender gender, Color color, Long cageNumber, boolean freed) {
        return jellyStockRepository.save(JellyEntity.builder()
                .gender(gender)
                .color(color)
                .cageNumber(cageNumber)
                .dateTimeFreed(freed ? LocalDateTime.now() : null)
                .build());
    }

    public JellyEntity saveNewJelly(Gender gender, Color color) {
        return saveNewJelly(gender, color, 1L, false);
    }

    public JellyEntity saveNewJelly(Gender gender) {
        return saveNewJelly(gender, BLUE, 1L, false);
    }

    public JellyEntity saveNewJelly(Long cageNumber) {
        return saveNewJelly(MALE, BLUE, cageNumber, false);
    }

    public void saveThreeBlueMales(int numberOfJellies, long cageNumber) {
        for(int i = 0; i < numberOfJellies ; i++) {
            saveNewJelly(cageNumber);
        }
    }

    public void cleanUp() {
        jellyStockRepository.deleteAll();
        cageRepository.deleteAll();
    }

    public JellyEntity saveFreedJelly() {
        return saveNewJelly(MALE, BLUE, 1L, true);
    }
}
