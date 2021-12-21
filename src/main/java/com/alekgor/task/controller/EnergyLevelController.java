package com.alekgor.task.controller;

import com.alekgor.task.common.RequestValidator;
import com.alekgor.task.common.exception.NotFoundResourceException;
import com.alekgor.task.model.entity.EnergyLevel;
import com.alekgor.task.service.EnergyLevelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/energyLevels")
public class EnergyLevelController {

    @Autowired
    private EnergyLevelServiceImpl energyLevelServiceImpl;

    @Autowired
    private RequestValidator validator;


    /**
     * Возвращает список текущих energyLevels
     */
    @GetMapping
    @Cacheable(value = "energyLevelListResponse", keyGenerator = "customKeyGenerator")
    public ResponseEntity<List<EnergyLevel>> getEnergyLevelList(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "50") Integer size) {
        validator.validatePage(page);
        validator.validateSize(size);

        return ResponseEntity.ok(energyLevelServiceImpl.findAllEnergyLevels(page, size));
    }

    /**
     * Возвращает высчитанный energyLevel
     */
    @GetMapping("/{isin}")
    @Cacheable(value = "energyLevelResponse", key = "#isin")
    public ResponseEntity<EnergyLevel> getEnergyLevel(@PathVariable String isin) {
        validator.validateIsin(isin);

        EnergyLevel result = energyLevelServiceImpl.findByIsin(isin);
        if (result == null) {
            throw new NotFoundResourceException("Not found energyLevel by ISIN");
        }

        return ResponseEntity.ok(result);
    }
}
