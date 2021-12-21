package com.alekgor.task.it;

import com.alekgor.task.controller.EnergyLevelController;
import com.alekgor.task.controller.QuoteController;
import com.alekgor.task.controller.dto.SaveQuoteRq;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.model.jpa.repository.EnergyLevelRepo;
import com.alekgor.task.model.jpa.repository.HistoryRepo;
import com.alekgor.task.model.jpa.repository.QuoteRepo;
import com.alekgor.task.service.EnergyLevelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class CacheIT {

    @Autowired
    private QuoteController quoteController;

    @Autowired
    private EnergyLevelController energyLevelController;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EnergyLevelServiceImpl energyLevelServiceImpl;

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private EnergyLevelRepo energyLevelRepo;

    @Autowired
    private QuoteRepo quoteRepo;

    @BeforeEach
    void setUp() {
        historyRepo.deleteAll();
        energyLevelRepo.deleteAll();
        quoteRepo.deleteAll();

        // сохраним котировки
        quoteController.saveQuote(new SaveQuoteRq("ABC123456789", 100.0, 105.0));
        quoteController.saveQuote(new SaveQuoteRq("000123456789", 10.0, 15.0));

        // вызовем методы контроллера, чтобы заполнился кеш 'energyLevelResponse'
        energyLevelController.getEnergyLevel("ABC123456789");
        energyLevelController.getEnergyLevel("000123456789");
    }

    /**
     * Проверим, что кеш 'energyLevelResponse' заполняется
     */
    @Test
    void testEnergyLevelInCache() {
        assertNotNull(cacheManager.getCache("energyLevelResponse").get("ABC123456789"));
        assertNotNull(cacheManager.getCache("energyLevelResponse").get("000123456789"));
    }

    /**
     * Проверим, что при обновлении котировки кеш 'energyLevelResponse' не сбрасывается для остальных котировок
     */
    @Test
    void testNotResetCacheForOtherIsin() {
        // обновим котировку новыми значениями
        quoteController.saveQuote(new SaveQuoteRq("ABC123456789", 1.0, 2.0));

        // проверим, что для котировок которые не были обновлены, значения остались в кеше
        ResponseEntity<EnergyLevel> response2 = (ResponseEntity) cacheManager.getCache("energyLevelResponse").get("000123456789").get();
        assertEquals("000123456789", response2.getBody().getQuote().getIsin());
    }

    /**
     * Проверим, что при обновлении котировки кеш очищается для конкретного ISIN
     */
    @Test
    void testResetCacheByIsin() {
        // обновим котировку новыми значениями
        quoteController.saveQuote(new SaveQuoteRq("ABC123456789", 1.0, 2.0));

        // проверим, что значение для обновленного ISIN стерто из кеша
        assertNull(cacheManager.getCache("energyLevelResponse").get("ABC123456789"));
    }
}
