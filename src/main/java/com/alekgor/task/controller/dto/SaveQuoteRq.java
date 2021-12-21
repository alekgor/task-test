package com.alekgor.task.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class SaveQuoteRq {
    @NotEmpty(message = "ISIN не долж")
    private String isin;
    private Double bid;
    private Double ask;
}
