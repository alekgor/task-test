package com.alekgor.task.unit.service;

import com.alekgor.task.model.Repository;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.History;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryServiceImplUnitTest {

    @InjectMocks
    private HistoryServiceImpl service;

    @Mock
    private Repository repository;

    @Captor
    ArgumentCaptor<History> historyArgumentCaptor;


    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(repository, repository);
    }


    @Test
    public void testSaveQuoteAndCalculateEnergyLevel() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);
        EnergyLevel energyLevel = new EnergyLevel(1L, quote, 1.0);

        service.save(quote, energyLevel);

        Mockito.verify(repository).save(historyArgumentCaptor.capture());

        History history = historyArgumentCaptor.getValue();
        assertEquals(2.0, history.getAsk());
        assertEquals(1.0, history.getBid());
        assertEquals(1.0, history.getEnergyLevel());
        assertEquals("AB1234567890", history.getIsin());
        assertNotNull(history.getDate());

        Mockito.verifyNoMoreInteractions(repository);
    }
}
