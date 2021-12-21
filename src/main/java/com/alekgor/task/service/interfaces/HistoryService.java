package com.alekgor.task.service.interfaces;

import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.Quote;

public interface HistoryService {

    /**
     * Записывает полученную котировку и ее energyLevel в историю,
     *
     * @param quote       котировка.
     * @param energyLevel energyLevel.
     */
    void save(Quote quote, EnergyLevel energyLevel);

}
