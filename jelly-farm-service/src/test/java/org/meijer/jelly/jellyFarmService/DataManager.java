package org.meijer.jelly.jellyFarmService;

import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;
import org.meijer.jelly.jellyFarmService.repository.CageRepository;
import org.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataManager {
    private final JellyStockRepository jellyStockRepository;
    private final CageRepository cageRepository;

    @Autowired
    public DataManager(JellyStockRepository jellyStockRepository, CageRepository cageRepository) {
        this.jellyStockRepository = jellyStockRepository;
        this.cageRepository = cageRepository;
    }

    public void createDefaultCage() {
        cageRepository.save(new CageEntity(1L, "Tropical Forest"));
    }


}
