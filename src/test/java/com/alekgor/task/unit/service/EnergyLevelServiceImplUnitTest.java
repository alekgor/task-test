package com.alekgor.task.unit.service;

import com.alekgor.task.model.Repository;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.EnergyLevelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EnergyLevelServiceImplUnitTest {

    @InjectMocks
    private EnergyLevelServiceImpl service;

    @Mock
    private Repository repository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(repository);
    }

    @Test
    public void testCalculate() {
        Quote quote = new Quote("AB1234567890", 1.0, 2.0);

        EnergyLevel result = service.calculate(quote);

        assertNull(result.getId());
        assertEquals(2.0, result.getQuote().getAsk());
        assertEquals(1.0, result.getQuote().getBid());
        assertEquals("AB1234567890", result.getQuote().getIsin());
        assertEquals(1.0, result.getEnergyLevel());
    }

    @Test
    public void testCalculateBidNull() {
        Quote quote = new Quote("AB1234567890", null, 2.0);

        EnergyLevel result = service.calculate(quote);

        assertNull(result.getQuote().getBid());
        assertNull(result.getId());
        assertEquals(2.0, result.getQuote().getAsk());
        assertEquals("AB1234567890", result.getQuote().getIsin());
        assertEquals(2.0, result.getEnergyLevel());
    }

    @Test
    public void testCalculateWhenEnergyLevelExistAndAskLessThenElvl() {
        Quote quote = new Quote("AB1234567890", 11.0, 15.0);
        EnergyLevel currentEnergyLevel = new EnergyLevel(123L, new Quote("AB1234567890", 20.0, 30.0), 20.0);

        Mockito.when(repository.findEnergyLevel(Mockito.eq("AB1234567890"))).thenReturn(currentEnergyLevel);

        EnergyLevel result = service.calculate(quote);

        assertEquals(123L, result.getId());
        assertEquals(15.0, result.getQuote().getAsk());
        assertEquals(11.0, result.getQuote().getBid());
        assertEquals("AB1234567890", result.getQuote().getIsin());
        assertEquals(15.0, result.getEnergyLevel());
    }

    @Test
    public void testCalculateWhenEnergyLevelExistAndBidMoreThenElvl() {
        Quote quote = new Quote("AB1234567890", 11.0, 15.0);
        EnergyLevel currentEnergyLevel = new EnergyLevel(123L, new Quote("AB1234567890", 1.0, 2.0), 1.0);

        Mockito.when(repository.findEnergyLevel(Mockito.eq("AB1234567890"))).thenReturn(currentEnergyLevel);

        EnergyLevel result = service.calculate(quote);

        assertEquals(123L, result.getId());
        assertEquals(15.0, result.getQuote().getAsk());
        assertEquals(11.0, result.getQuote().getBid());
        assertEquals("AB1234567890", result.getQuote().getIsin());
        assertEquals(11.0, result.getEnergyLevel());
    }

    @Test
    public void testSave() {
        EnergyLevel energyLevel = new EnergyLevel(123L, new Quote("AB1234567890", 1.0, 2.0), 1.0);

        service.save(energyLevel);

        Mockito.verify(repository).save(Mockito.eq(energyLevel));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindByIsin() {
        service.findByIsin("AB1234567890");

        Mockito.verify(repository).findEnergyLevel(Mockito.eq("AB1234567890"));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindAll() {
        service.findAllEnergyLevels(1, 10);

        Mockito.verify(repository).findAllEnergyLevels(1, 10);
        Mockito.verifyNoMoreInteractions(repository);
    }

}
