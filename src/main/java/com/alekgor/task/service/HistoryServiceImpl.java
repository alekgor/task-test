package com.alekgor.task.service;

import com.alekgor.task.model.Repository;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.History;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.interfaces.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private Repository historyRepo;

    @Override
    public void save(Quote quote, EnergyLevel energyLevel) {
        historyRepo.save(History.builder()
                .isin(quote.getIsin())
                .ask(quote.getAsk())
                .bid(quote.getBid())
                .energyLevel(energyLevel.getEnergyLevel())
                .date(new Date())
                .build()
        );
    }
}
