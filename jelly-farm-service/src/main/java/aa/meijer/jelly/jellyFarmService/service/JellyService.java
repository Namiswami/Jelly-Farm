package aa.meijer.jelly.jellyFarmService.service;

import aa.meijer.jelly.jellyFarmService.exception.CageNotFoundException;
import aa.meijer.jelly.jellyFarmService.exception.JellyNotFoundException;
import aa.meijer.jelly.jellyFarmService.model.cage.Cage;
import aa.meijer.jelly.jellyFarmService.model.cage.CageOverview;
import aa.meijer.jelly.jellyFarmService.model.jelly.Jelly;
import aa.meijer.jelly.jellyFarmService.model.jelly.JellyOverview;
import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import aa.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import aa.meijer.jelly.jellyFarmService.repository.JellyStockRepository;
import aa.meijer.jelly.jellyFarmService.repository.entity.JellyEntity;
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

    public JellyOverview getJellyOverview() {
        List<JellyEntity> allJellies = jellyStockRepository.findAll();
        return new JellyOverview(allJellies);
    }

    public CageOverview getCageOverview() {
        List<Cage> cages = cageService.getAllCages();
        for(Cage cage : cages) {
            JellyOverview overview = new JellyOverview(jellyStockRepository.findByCageNumberUnsold(cage.getCageNumber()));
            cage.setJellyOverview(overview);
        }
        return new CageOverview(cages);
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

    public Jelly sellJelly(UUID id) {
        Optional<JellyEntity> jellyOptional = jellyStockRepository.findById(id);

        if(jellyOptional.isPresent()) {
            JellyEntity jelly = jellyOptional.get();
            jelly.setDateTimeFreed(LocalDateTime.now());
            jellyStockRepository.save(jelly);
            return new Jelly(jelly);
        } else throw new JellyNotFoundException();

    }

    public void deleteAll() {
        jellyStockRepository.deleteAll();
    }

    public List<Jelly> getJelliesByCage(Long cageNumber) {
        if(!cageService.existsById(cageNumber)) throw new CageNotFoundException();

        List<JellyEntity> entityList = jellyStockRepository.findByCageNumberUnsold(cageNumber);
        return entityList.stream()
                .map(Jelly::new)
                .collect(Collectors.toList());
    }

    public List<Jelly> getAllJellies() {
        List<JellyEntity> entityList = jellyStockRepository.findAll();
        return entityList.stream()
                .map(Jelly::new)
                .collect(Collectors.toList());
    }

    public void save(Jelly newBornJelly) {
        jellyStockRepository.save(new JellyEntity(newBornJelly));
    }
}