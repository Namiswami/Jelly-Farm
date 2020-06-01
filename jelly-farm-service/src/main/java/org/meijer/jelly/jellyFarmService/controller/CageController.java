package org.meijer.jelly.jellyFarmService.controller;

import org.meijer.jelly.jellyFarmService.model.cage.dto.CageListDTO;
import org.meijer.jelly.jellyFarmService.service.CageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("v1/cage")
public class CageController {
    private final CageService cageService;

    @Autowired
    public CageController(CageService cageService) {
        this.cageService = cageService;
    }

    @GetMapping("/all")
    public ResponseEntity<CageListDTO> getAllCages() {
        return ResponseEntity.ok(cageService.getAllCages());
    }

}
