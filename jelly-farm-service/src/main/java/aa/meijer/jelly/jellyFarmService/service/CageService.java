package aa.meijer.jelly.jellyFarmService.service;

import aa.meijer.jelly.jellyFarmService.model.cage.Cage;
import aa.meijer.jelly.jellyFarmService.repository.CageRepository;
import aa.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
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

    public List<Cage> getAllCages() {
        List<CageEntity> entityList = cageRepository.findAll();
        return entityList.stream()
                .map(Cage::new)
                .collect(Collectors.toList());
    }
}
