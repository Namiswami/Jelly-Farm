package org.meijer.jelly.jellyFarmService.repository;

import org.meijer.jelly.jellyFarmService.repository.entity.CageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CageRepository extends JpaRepository<CageEntity, Long> {
}
