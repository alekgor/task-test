package com.alekgor.task.unit.service;

import com.alekgor.task.model.Repository;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.QuoteServiceImpl;
import com.alekgor.task.service.interfaces.EnergyLevelService;
import com.alekgor.task.service.interfaces.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class QuoteServiceImplUnitTest {

    @InjectMocks
    private QuoteServiceImpl service;

    @Mock
    private Repository repository;

    @Mock
    private EnergyLevelService energyLevelService;

    @Mock
    private HistoryService historyService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(repository, repository, historyService);
    }


    @Test
    public void testSaveQuoteAndCalculateEnergyLevel() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);
        EnergyLevel energyLevel = new EnergyLevel(1L, quote, 1.0);

        Mockito.when(energyLevelService.calculate(quote)).thenReturn(energyLevel);
        Mockito.when(repository.save(quote)).thenReturn(quote);

        service.saveQuoteAndCalculateEnergyLevel(quote);

        Mockito.verify(energyLevelService).calculate(Mockito.eq(quote));
        Mockito.verify(repository).save(Mockito.eq(quote));
        Mockito.verify(energyLevelService).save(Mockito.eq(energyLevel));
        Mockito.verify(historyService).save(Mockito.eq(quote), Mockito.eq(energyLevel));

        Mockito.verifyNoMoreInteractions(energyLevelService, repository, historyService);
    }

}
