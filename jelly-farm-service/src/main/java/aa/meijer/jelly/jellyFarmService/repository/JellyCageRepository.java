package aa.meijer.jelly.jellyFarmService.repository;

import aa.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JellyCageRepository extends JpaRepository<CageEntity, Integer> {
}
