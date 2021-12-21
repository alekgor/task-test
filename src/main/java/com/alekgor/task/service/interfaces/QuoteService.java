package com.alekgor.task.service.interfaces;

import com.alekgor.task.model.entity.Quote;

public interface QuoteService {

    /**
     * Сохраняет котировку и считает для нее energyLevel.
     * Если существует котировка с таким же ISIN, то обновляет ее поля и energyLevel.
     *
     * @param quote котировка
     * @return сохраненая сущность
     */
    Quote saveQuoteAndCalculateEnergyLevel(Quote quote);
}
