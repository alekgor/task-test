package com.alekgor.task.common;

import com.alekgor.task.controller.dto.SaveQuoteRq;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.ValidationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * Сервис для валидации данных
 */
@Component
@Validated
public class RequestValidator {

    public void validatePage(@Min(value = 0, message = "Page number 'page' must be positive.")
                                     Integer page) {

    }

    public void validateSize(@Min(value = 1, message = "Page size 'size' must be positive.")
                             @Max(value = 50, message = "Page size 'size' must be less {value}.")
                                     Integer size) {
    }

    public void validateIsin(@Pattern(regexp = "\\w{12}", message = "ISIN must be contains 12 characters.") String isin) {
    }

    public void validateBidAndAsk(SaveQuoteRq rq) {
        if (rq.getBid() > rq.getAsk()) {
            throw new ValidationException("'bid' must be less 'ask'.");
        }
    }
}
