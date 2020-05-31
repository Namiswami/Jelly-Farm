package aa.meijer.jellyfarm.service;

import aa.meijer.jellyfarm.exception.CageNotFoundException;
import aa.meijer.jellyfarm.exception.JellyNotFoundException;
import aa.meijer.jellyfarm.model.cage.Cage;
import aa.meijer.jellyfarm.model.cage.CageOverview;
import aa.meijer.jellyfarm.model.jelly.Jelly;
import aa.meijer.jellyfarm.model.jelly.JellyOverview;
import aa.meijer.jellyfarm.model.jelly.attributes.Color;
import aa.meijer.jellyfarm.model.jelly.attributes.Gender;
import aa.meijer.jellyfarm.repository.JellyCageRepository;
import aa.meijer.jellyfarm.repository.JellyStockRepository;
import aa.meijer.jellyfarm.repository.entity.CageEntity;
import aa.meijer.jellyfarm.repository.entity.JellyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class JellyDetailsService {
    private JellyStockRepository jellyStockRepository;
    private JellyCageRepository jellyCageRepository;
    private KafkaTemplate kafkaTemplate;

    @Autowired
    public JellyDetailsService(JellyStockRepository jellyStockRepository,
                               JellyCageRepository jellyCageRepository,
                               KafkaTemplate kafkaTemplate) {
        this.jellyStockRepository = jellyStockRepository;
        this.jellyCageRepository = jellyCageRepository;
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
        List<CageEntity> cageEntities = jellyCageRepository.findAll();
        List<Cage> cages = new ArrayList<>();
        for(CageEntity cage : cageEntities) {
            JellyOverview overview = new JellyOverview(jellyStockRepository.findByCageNumberUnsold(cage.getCageNumber()));
            cages.add(new Cage(cage, overview));
        }
        return new CageOverview(cages);
    }

    public void buyNewJelly(Color color, Gender gender, int cageNumber) {
        if(!jellyCageRepository.existsById(cageNumber)) {
            log.error("No cage with cage number {}", cageNumber);
            throw new CageNotFoundException();
        }

        JellyEntity jelly = JellyEntity.builder()
                .color(color)
                .cageNumber(cageNumber)
                .gender(gender)
                .dateTimeSold(null)
                .build();

        jellyStockRepository.save(jelly);

    }

    public Jelly sellJelly(UUID id) {
        Optional<JellyEntity> jellyOptional = jellyStockRepository.findById(id);

        if(jellyOptional.isPresent()) {
            JellyEntity jelly = jellyOptional.get();
            jelly.setDateTimeSold(LocalDateTime.now());
            jellyStockRepository.save(jelly);
            return new Jelly(jelly);
        } else throw new JellyNotFoundException();

    }

    public void deleteAll() {
        jellyStockRepository.deleteAll();
    }

}
