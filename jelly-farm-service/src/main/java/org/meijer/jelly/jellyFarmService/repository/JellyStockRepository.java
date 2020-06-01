package org.meijer.jelly.jellyFarmService.repository;

import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JellyStockRepository extends JpaRepository<JellyEntity, UUID> {

    @Query("FROM JellyEntity j " +
            "WHERE j.dateTimeFreed = NULL " +
            "AND j.cageNumber = :cageNumber")
    List<JellyEntity> findByCageNumberUnsold(long cageNumber);
}
