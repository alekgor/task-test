package com.alekgor.task.service.interfaces;

import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.Quote;

import java.util.List;

public interface EnergyLevelService {

    /**
     * Считает energyLevel для заданной котировки используя bid и ask.
     *
     * @param quote котировка.
     * @return energyLevel.
     */
    EnergyLevel calculate(Quote quote);


    /**
     * Сохраняет energyLevel.
     *
     * @return сохраненную сущность.
     */
    EnergyLevel save(EnergyLevel energyLevel);


    /**
     * Предоставляет energyLevel для конкретной котровки по ISIN.
     *
     * @param isin код котировки.
     * @return energyLevel.
     */
    EnergyLevel findByIsin(String isin);


    /**
     * Предоставляет список energyLevels c учетом пагинации.
     *
     * @param page номер страницы.
     * @param pageSize размер страницы.
     * @return список energyLevels.
     */
    List<EnergyLevel> findAllEnergyLevels(Integer page, Integer pageSize);

}
