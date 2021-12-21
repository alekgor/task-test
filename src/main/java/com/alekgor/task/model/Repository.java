package com.alekgor.task.model;

import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.History;
import com.alekgor.task.model.entity.Quote;

import java.util.List;

/**
 * Сервис для работы со слоем представления.
 */
public interface Repository {

    Quote save(Quote quote);

    EnergyLevel save(EnergyLevel energyLevel);

    History save(History history);

    EnergyLevel findEnergyLevel(String isin);

    List<EnergyLevel> findAllEnergyLevels(Integer page, Integer pageSize);

}
