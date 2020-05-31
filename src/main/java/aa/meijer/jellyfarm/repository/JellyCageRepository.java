package aa.meijer.jellyfarm.repository;

import aa.meijer.jellyfarm.repository.entity.CageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JellyCageRepository extends JpaRepository<CageEntity, Integer> {
}
