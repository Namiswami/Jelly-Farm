package org.meijer.jelly.jellyFarmService.service;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.entity.CageEntity;
import org.meijer.jelly.jellyFarmService.repository.CageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public CageOverviewDTO getSingleCageOverview(Long cageNumber) {
        Optional<CageEntity> optionalCageEntity = cageRepository.findById(cageNumber);
        if(optionalCageEntity.isPresent()) return new CageOverviewDTO(optionalCageEntity.get());
        else {
            log.error("Cage with Number {} does not exist", cageNumber);
            throw new CageNotFoundException(cageNumber);
        }
    }

    public List<CageOverviewDTO> getCageOverview() {
        return cageRepository.findAll().stream()
                .map(CageOverviewDTO::new)
                .collect(Collectors.toList());
    }

}
