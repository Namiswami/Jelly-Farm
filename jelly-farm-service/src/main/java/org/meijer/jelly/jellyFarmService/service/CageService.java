package org.meijer.jelly.jellyFarmService.service;

import org.meijer.jelly.jellyFarmService.model.cage.CageDTO;
import org.meijer.jelly.jellyFarmService.model.cage.CageListDTO;
import org.meijer.jelly.jellyFarmService.model.cage.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.repository.CageRepository;
import org.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CageService {
    private final CageRepository cageRepository;

    @Autowired
    public CageService(CageRepository cageRepository) {
        this.cageRepository = cageRepository;
    }

    public boolean existsById(long cageNumber) {
        return cageRepository.existsById(cageNumber);
    }

    public CageListDTO getAllCages() {
        List<CageEntity> entityList = cageRepository.findAll();
        return new CageListDTO(entityList.stream()
                .map(CageDTO::new)
                .collect(Collectors.toList()));
    }

    public List<CageOverviewDTO> getOverview() {
        List<CageEntity> entityList = cageRepository.findAll();
        return entityList.stream()
                .map(CageOverviewDTO::new)
                .collect(Collectors.toList());
    }
}
