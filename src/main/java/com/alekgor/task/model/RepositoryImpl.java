package com.alekgor.task.model;

import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.History;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.model.jpa.repository.EnergyLevelRepo;
import com.alekgor.task.model.jpa.repository.HistoryRepo;
import com.alekgor.task.model.jpa.repository.QuoteRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RepositoryImpl implements Repository {

    @Autowired
    private QuoteRepo quoteRepo;

    @Autowired
    private EnergyLevelRepo energyLevelRepo;

    @Autowired
    private HistoryRepo historyRepo;

    @Override
    public Quote save(Quote quote) {
        return quoteRepo.save(quote);
    }

    @Override
    public EnergyLevel save(EnergyLevel energyLevel) {
        if (energyLevel.getEnergyLevel() == null){
          log.info("er");
        }
        return energyLevelRepo.save(energyLevel);
    }

    @Override
    public History save(History history) {
        return historyRepo.save(history);
    }

    @Override
    public EnergyLevel findEnergyLevel(String isin) {
        return energyLevelRepo.findByIsin(isin);
    }

    @Override
    public List<EnergyLevel> findAllEnergyLevels(Integer page, Integer pageSize) {
        return energyLevelRepo.findAll(PageRequest.of(page, pageSize)).getContent();
    }
}
