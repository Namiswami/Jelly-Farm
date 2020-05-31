package aa.meijer.jelly.jellyFarmService.service;

import aa.meijer.jelly.jellyFarmService.model.jelly.Jelly;
import aa.meijer.jelly.jellyFarmService.model.jelly.JellyCouple;
import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import aa.meijer.jelly.jellyFarmService.repository.JellyCageRepository;
import aa.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import aa.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
import aa.meijer.jelly.jellyFarmService.repository.entity.JellyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JellyBreedingService {
    private final Integer cageLimit;
    private final JellyStockRepository jellyStockRepository;
    private final JellyCageRepository jellyCageRepository;

    @Autowired
    public JellyBreedingService(@Value("${cage.limit}") Integer cageLimit,
                                JellyStockRepository jellyStockRepository,
                                JellyCageRepository jellyCageRepository) {
        this.cageLimit = cageLimit;
        this.jellyStockRepository = jellyStockRepository;
        this.jellyCageRepository = jellyCageRepository;
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void breedAllCages() {
        List<CageEntity> cages = jellyCageRepository.findAll();
        for (CageEntity cage: cages) {
            breed(cage);
        }

    }

    private void breed(CageEntity cage) {
        List<Jelly> jellies = getUnsoldJellies(cage.getCageNumber());
        List<Jelly> availableFemales = filterByGender(Gender.FEMALE, jellies);
        List<Jelly> males = filterByGender(Gender.FEMALE, jellies);
        List<JellyEntity> newBornJellies = new ArrayList<>();

        for (Jelly male : males) {
            if (jellies.size() + newBornJellies.size() < cageLimit) {
                JellyCouple couple = male.formCouple(availableFemales);

                if (couple != null) {
                    availableFemales.remove(couple.getMother());
                    newBornJellies.add(new JellyEntity(couple.mate()));
                }
            } else {
                log.info("Cage {} is full, breeding was stopped", cage.getCageNumber());
                break;
            }
        }

        log.info("{} new jellies have been born in {} cage: {}", newBornJellies.size(), cage.getHabitatName(), cage.getCageNumber());
        jellyStockRepository.saveAll(newBornJellies);

    }

    private List<Jelly> getUnsoldJellies(int cageNumber) {
        return jellyStockRepository.findByCageNumberUnsold(cageNumber).stream()
                .map(Jelly::new)
                .collect(Collectors.toList());
    }

    private List<Jelly> filterByGender(Gender gender, List<Jelly> jellies) {
        return jellies.stream()
                .filter(j -> j.getGender() == gender)
                .collect(Collectors.toList());
    }
}
