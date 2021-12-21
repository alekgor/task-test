package com.alekgor.task.it;

import com.alekgor.task.controller.EnergyLevelController;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.model.jpa.repository.EnergyLevelRepo;
import com.alekgor.task.model.jpa.repository.HistoryRepo;
import com.alekgor.task.model.jpa.repository.QuoteRepo;
import com.alekgor.task.service.interfaces.QuoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnergyLevelControllerIT {

    private static final String VALID_ISIN = "RU000A0JX0J2";

    @Autowired
    private EnergyLevelController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private HistoryRepo historyRepo;
    @Autowired
    private EnergyLevelRepo energyLevelRepo;
    @Autowired
    private QuoteRepo quoteRepo;


    @BeforeEach
    public void clearRepo() {
        historyRepo.deleteAll();
        energyLevelRepo.deleteAll();
        quoteRepo.deleteAll();

        Quote quote = new Quote(VALID_ISIN, 1.0, 2.0);
        // наполним бд
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);
    }


    /**
     * Получение списка energyLevels
     */
    @Test
    public void testGetEnergyLevelList() throws Exception {
        mockMvc.perform(get("/energyLevels"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J2\",\"bid\":1.0,\"ask\":2.0},\"energyLevel\":1.0")),
                        content().string(containsString("\"energyLevel\":1.0"))
                );
    }

    /**
     * Изменим текущую котировку, и получим список
     */
    @Test
    public void testGetEnergyLevelListAfterEdit() throws Exception {
        Quote quote = new Quote(VALID_ISIN, 100.0, 200.0);
        // наполним бд
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);


        mockMvc.perform(get("/energyLevels"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J2\",\"bid\":100.0,\"ask\":200.0},\"energyLevel\":100.0}"))
                );
    }

    /**
     * Добавим новую котировку, и получим список
     */
    @Test
    public void testGetEnergyLevelListAfterAddNew() throws Exception {
        Quote quote = new Quote("RU000A0JX0J3", 505.0, 510.0);
        // наполним бд
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);


        mockMvc.perform(get("/energyLevels"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J2\",\"bid\":1.0,\"ask\":2.0},\"energyLevel\":1.0")),
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J3\",\"bid\":505.0,\"ask\":510.0},\"energyLevel\":505.0}"))
                );
    }

    /**
     * Изменим текущую котировку, и получим список
     */
    @Test
    public void testGetEnergyLevelListPaging() throws Exception {
        Quote quote = new Quote("RU000A0JX0J3", 333.0, 400.0);
        // добавим еще одну котировку, чтобы хранилось две
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J2\",\"bid\":1.0,\"ask\":2.0},\"energyLevel\":1.0"))
                );
        // получем результат второй страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels?page=1&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J3\",\"bid\":333.0,\"ask\":400.0},\"energyLevel\":333.0"))
                );
    }

    /**
     * Ошибка валидации, при отрицательном номере страницы
     */
    @Test
    public void testGetEnergyLevelListValidExceptionNegativePage() throws Exception {

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels?page=-1&size=1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        content().string(containsString("\"status\":\"BAD_REQUEST\"")),
                        content().string(containsString("Validation error:")),
                        content().string(containsString("Page number 'page' must be positive."))
                );
    }

    /**
     * Ошибка валидации, при превышении максимального размера страницы
     */
    @Test
    public void testGetEnergyLevelListValidExceptionPageSizeOverlimit() throws Exception {

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels?page=1&size=2341"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        content().string(containsString("\"status\":\"BAD_REQUEST\"")),
                        content().string(containsString("Validation error:")),
                        content().string(containsString("Page size 'size' must be less 50."))
                );
    }

    /**
     * Получение energyLevel по ISIN
     */
    @Test
    public void testGetEnergyLevel() throws Exception {

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels/RU000A0JX0J2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J2\",\"bid\":1.0,\"ask\":2.0},\"energyLevel\":1.0"))
                );
    }

    /**
     * Получение energyLevel по ISIN, после обнолвения
     */
    @Test
    public void testGetEnergyLevelAfterEdit() throws Exception {
        Quote quote = new Quote(VALID_ISIN, 100.0, 200.0);
        // наполним бд
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels/RU000A0JX0J2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(containsString("\"quote\":{\"isin\":\"RU000A0JX0J2\",\"bid\":100.0,\"ask\":200.0},\"energyLevel\":100.0"))
                );
    }

    /**
     * Невалидный ISIN
     */
    @Test
    public void testGetEnergyLevelValidExceptionIsin() throws Exception {
        Quote quote = new Quote(VALID_ISIN, 100.0, 200.0);
        // наполним бд
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels/RU000 0JX0J2"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        content().string(containsString("\"status\":\"BAD_REQUEST\"")),
                        content().string(containsString("Validation error:")),
                        content().string(containsString("ISIN must be contains 12 characters."))
                );
    }

    /**
     * Не найден energyLevel
     */
    @Test
    public void testGetEnergyLevelNotFound() throws Exception {
        Quote quote = new Quote(VALID_ISIN, 100.0, 200.0);
        // наполним бд
        quoteService.saveQuoteAndCalculateEnergyLevel(quote);

        // получем результат первой страницы с кол-вом элементов равным 1
        mockMvc.perform(get("/energyLevels/NOTEXISTCODE"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpectAll(
                        content().string(containsString("\"status\":\"NOT_FOUND\"")),
                        content().string(containsString("Not found error:")),
                        content().string(containsString("Not found energyLevel by ISIN")
                        )
                );
    }
}
