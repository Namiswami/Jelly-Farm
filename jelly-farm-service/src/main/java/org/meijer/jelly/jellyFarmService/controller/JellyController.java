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
public class JellyController {
    private JellyService jellyService;

    @Autowired
    public JellyController(JellyService jellyService) {
        this.jellyService = jellyService;
    }

    @PostMapping("/buy")
    public ResponseEntity buyJelly(@RequestParam(name = "color") Color color,
                                   @RequestParam(name = "gender") Gender gender,
                                   @RequestParam(name = "cageNumber") long cageNumber) {
        log.info("Buying a new jelly from the jelly salesman and adding it to our farm");
        jellyService.buyNewJelly(color, gender, cageNumber);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/sales")
    public ResponseEntity<JellyDTO> sellJelly(@RequestParam(name = "uuid") UUID id) {
        log.info("We sold a jelly! For money! Yay");
        return ResponseEntity.ok(jellyService.sellJelly(id));
    }


    @DeleteMapping("/deleteAll")
    public ResponseEntity deleteAll() {
        jellyService.deleteAll();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/cage")
    public ResponseEntity<CageOverviewListDTO> getCages() {
        return new ResponseEntity(jellyService.getCageOverview(), HttpStatus.OK);
    }
}
