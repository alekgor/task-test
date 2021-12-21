package com.alekgor.task.controller;


import com.alekgor.task.common.Converter;
import com.alekgor.task.common.RequestValidator;
import com.alekgor.task.controller.dto.SaveQuoteRq;
import com.alekgor.task.model.entity.Quote;
import com.alekgor.task.service.QuoteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class QuoteController {

    @Autowired
    private QuoteServiceImpl quoteService;

    @Autowired
    private RequestValidator validator;


    /**
     * Получает котировку и считает для нее energyLevel
     */
    @PutMapping("quote")
    public ResponseEntity<Quote> saveQuote(@RequestBody SaveQuoteRq request) {
        validator.validateBidAndAsk(request);
        validator.validateIsin(request.getIsin());

        Quote quote = quoteService.saveQuoteAndCalculateEnergyLevel(Converter.convert(request));

        return new ResponseEntity<>(quote, HttpStatus.CREATED);
    }
}
