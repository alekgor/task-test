package com.alekgor.task.service;

import com.alekgor.task.model.Repository;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.interfaces.EnergyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyLevelServiceImpl implements EnergyLevelService {

    @Autowired
    private Repository repository;

    public EnergyLevel calculate(Quote quote) {
        EnergyLevel energyLevel = repository.findEnergyLevel(quote.getIsin());
        Double result = null;

        if (energyLevel == null) {
            if (quote.getBid() != null) {
                result = quote.getBid();
            } else {
                result = quote.getAsk();
            }
        } else if (quote.getBid() > energyLevel.getEnergyLevel()) {
            result = quote.getBid();
        } else if (quote.getAsk() < energyLevel.getEnergyLevel()) {
            result = quote.getAsk();
        }

        return EnergyLevel.builder()
                .id(energyLevel != null ? energyLevel.getId() : null)
                .quote(quote)
                .energyLevel(result)
                .build();
    }

    @CacheEvict(value = "energyLevelResponse", key = "#energyLevel.getQuote().getIsin()")
    public EnergyLevel save(EnergyLevel energyLevel) {
        return repository.save(energyLevel);
    }

    public EnergyLevel findByIsin(String isin) {
        return repository.findEnergyLevel(isin);
    }

    public List<EnergyLevel> findAllEnergyLevels(Integer page, Integer pageSize) {
        return repository.findAllEnergyLevels(page, pageSize);
    }
}
