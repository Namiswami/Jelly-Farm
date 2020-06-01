package org.meijer.jelly.jellyFarmService.controller;

import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewListDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@Slf4j
public class JellyAdoptionController {
    private JellyService jellyService;

    @Autowired
    public JellyAdoptionController(JellyService jellyService) {
        this.jellyService = jellyService;
    }


    @GetMapping("/cage")
    public ResponseEntity<CageOverviewListDTO> getCages() {
        return new ResponseEntity(jellyService.getCageOverview(), HttpStatus.OK);
    }
}
