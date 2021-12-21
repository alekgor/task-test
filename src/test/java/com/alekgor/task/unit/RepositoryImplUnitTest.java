package com.alekgor.task.unit;

import com.alekgor.task.model.RepositoryImpl;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.History;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.model.jpa.repository.EnergyLevelRepo;
import com.alekgor.task.model.jpa.repository.HistoryRepo;
import com.alekgor.task.model.jpa.repository.QuoteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Date;


class RepositoryImplUnitTest {

    @InjectMocks
    private RepositoryImpl repository;

    @Mock
    private QuoteRepo quoteRepo;

    @Mock
    private EnergyLevelRepo energyLevelRepo;

    @Mock
    private HistoryRepo historyRepo;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllEnergyLevels() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);
        EnergyLevel energyLevel = new EnergyLevel(1L, quote, 1.0);


        Mockito.when(energyLevelRepo.findAll(Mockito.eq(PageRequest.of(1, 50))))
                .thenReturn(new PageImpl<>(Collections.singletonList(energyLevel)));

        repository.findAllEnergyLevels(1, 50);

        Mockito.verify(energyLevelRepo).findAll(Mockito.eq(PageRequest.of(1, 50)));
        Mockito.verifyNoMoreInteractions(energyLevelRepo);
    }

    @Test
    public void testFindEnergyLevelByIsin() {

        repository.findEnergyLevel("AB1234567890");

        Mockito.verify(energyLevelRepo).findByIsin(Mockito.eq("AB1234567890"));
        Mockito.verifyNoMoreInteractions(energyLevelRepo);
    }

    @Test
    public void testSaveHistory() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);
        EnergyLevel energyLevel = new EnergyLevel(1L, quote, 1.0);
        History history = History.builder()
                .isin(quote.getIsin())
                .date(new Date())
                .bid(quote.getBid())
                .ask(quote.getAsk())
                .energyLevel(energyLevel.getEnergyLevel())
                .build();


        repository.save(history);

        Mockito.verify(historyRepo).save(Mockito.eq(history));
        Mockito.verifyNoMoreInteractions(historyRepo);
    }

    @Test
    public void testSaveQuote() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);

        repository.save(quote);

        Mockito.verify(quoteRepo).save(Mockito.eq(quote));
        Mockito.verifyNoMoreInteractions(quoteRepo);
    }

    @Test
    public void testSaveEnergyLevel() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);
        EnergyLevel energyLevel = new EnergyLevel(1L, quote, 1.0);

        repository.save(energyLevel);

        Mockito.verify(energyLevelRepo).save(Mockito.eq(energyLevel));
        Mockito.verifyNoMoreInteractions(energyLevelRepo);
    }

}
