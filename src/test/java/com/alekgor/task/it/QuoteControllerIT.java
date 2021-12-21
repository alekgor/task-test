package com.alekgor.task.it;

import com.alekgor.task.controller.QuoteController;
import com.alekgor.task.controller.dto.SaveQuoteRq;
import com.alekgor.task.model.jpa.repository.EnergyLevelRepo;
import com.alekgor.task.model.jpa.repository.HistoryRepo;
import com.alekgor.task.model.jpa.repository.QuoteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuoteControllerIT {

    private static final String VALID_ISIN = "RU000A0JX0J2";

    @Autowired
    private QuoteController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HistoryRepo historyRepo;
    @Autowired
    private EnergyLevelRepo energyLevelRepo;
    @Autowired
    private QuoteRepo quoteRepo;

    @BeforeTestClass
    public void clearRepo() {
        historyRepo.deleteAll();
        energyLevelRepo.deleteAll();
        quoteRepo.deleteAll();
    }

    /**
     * Обычное добавление котировки в пустую БД.
     */
    @Test
    public void testSaveQuote() throws Exception {
        SaveQuoteRq req = new SaveQuoteRq(VALID_ISIN, 100.2, 101.9);

        mockMvc.perform(
                        put("/quote")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(
                        content().string(containsString("\"isin\":\"RU000A0JX0J2\"")),
                        content().string(containsString("\"bid\":100.2")),
                        content().string(containsString("\"ask\":101.9"))
                );
    }

    /**
     * Ошибка валидации данных, когда bid больше ask.
     */
    @Test
    public void testSaveQuoteBidMoreAskValidException() throws Exception {
        SaveQuoteRq req = new SaveQuoteRq(VALID_ISIN, 105.2, 101.9);

        mockMvc.perform(
                        put("/quote")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        content().string(containsString("\"status\":\"BAD_REQUEST\"")),
                        content().string(containsString("Validation error: 'bid' must be less 'ask'")),
                        content().string(containsString("timestamp"))
                );
    }

    /**
     * Ошибка валидации данных, невалидный ISIN
     */
    @Test
    public void testSaveQuoteInvalidISIN() throws Exception {
        SaveQuoteRq req = new SaveQuoteRq("", 101.2, 101.9);

        mockMvc.perform(
                        put("/quote")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        content().string(containsString("\"status\":\"BAD_REQUEST\"")),
                        content().string(containsString("Validation error:")),
                        content().string(containsString("ISIN must be contains 12 characters.")),
                        content().string(containsString("timestamp"))
                );
    }

    /**
     * Ошибка валидации данных, невалидный ISIN
     */
    @Test
    public void testSaveQuoteInvalidISINWithSpaces() throws Exception {
        SaveQuoteRq req = new SaveQuoteRq("RU000  JX0J2", 101.2, 101.9);

        mockMvc.perform(
                        put("/quote")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        content().string(containsString("\"status\":\"BAD_REQUEST\"")),
                        content().string(containsString("Validation error:")),
                        content().string(containsString("ISIN must be contains 12 characters.")),
                        content().string(containsString("timestamp"))
                );
    }


    /**
     * Неправильный путь
     */
    @Test
    public void testWrongPath() throws Exception {
        SaveQuoteRq req = new SaveQuoteRq(VALID_ISIN, 101.2, 101.9);

        mockMvc.perform(
                        put("/quote/1")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
