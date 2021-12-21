package com.alekgor.task.common;

import com.alekgor.task.controller.dto.SaveQuoteRq;
import com.alekgor.task.model.entity.Quote;

/**
 * Сервис конвертации dto в сущности.
 */
public class Converter {

    private Converter() {
    }

    public static Quote convert(SaveQuoteRq request) {
        return Quote.builder()
                .isin(request.getIsin())
                .ask(request.getAsk())
                .bid(request.getBid())
                .build();
    }

}
