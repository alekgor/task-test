package com.alekgor.task.service;

import com.alekgor.task.model.Repository;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.interfaces.EnergyLevelService;
import com.alekgor.task.service.interfaces.HistoryService;
import com.alekgor.task.service.interfaces.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private EnergyLevelService energyLevelService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private Repository repository;

    @Autowired
    private CacheManager cacheManager;

    @Override
    @Transactional
    @CacheEvict(value = "energyLevelListResponse", allEntries = true)
    public Quote saveQuoteAndCalculateEnergyLevel(Quote quote) {
        Quote result = repository.save(quote);

        EnergyLevel energyLevel = energyLevelService.calculate(quote);
        energyLevelService.save(energyLevel);

        historyService.save(quote, energyLevel);

        return result;
    }
}
