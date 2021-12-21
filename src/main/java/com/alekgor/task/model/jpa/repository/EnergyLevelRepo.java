package com.alekgor.task.model.jpa.repository;

import com.alekgor.task.model.entity.EnergyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnergyLevelRepo extends JpaRepository<EnergyLevel, Long> {

    @Query("FROM EnergyLevel AS el INNER JOIN el.quote AS q WHERE q.isin = :isin")
    EnergyLevel findByIsin(@Param("isin") String isin);
}
