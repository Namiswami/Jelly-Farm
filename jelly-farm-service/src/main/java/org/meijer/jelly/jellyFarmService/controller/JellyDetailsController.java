package org.meijer.jelly.jellyFarmService.controller;

import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewListDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyListDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.entity.JellyEntity;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/v1/details")
public class JellyDetailsController {
    private final JellyService jellyService;

    @Autowired
    public JellyDetailsController(JellyService jellyService) {
        this.jellyService = jellyService;
    }

    @GetMapping()
    public ResponseEntity<JellyEntity> getJelly(@RequestParam(name = "uuid") UUID id) {
        log.info("Getting details for single jelly");
        return ResponseEntity.ok(jellyService.getJelly(id));
    }

    @GetMapping("/stock")
    public ResponseEntity<JellyListDTO> getStock(@RequestParam(name = "cageNumber", required = false) Long cageNumber) {
        List<JellyDTO> result;
        if (cageNumber == null) {
            log.info("Received request for list of all jellies");
            result = jellyService.getAllJellies();
        } else {
            log.info("Received request for list of all jellies in cage number {}", cageNumber);
            result = jellyService.getJelliesByCage(cageNumber);
        }
        return ResponseEntity.ok(new JellyListDTO(result));
    }

    @GetMapping("/overview")
    public ResponseEntity<JellyOverviewDTO> getAllJellies() {
        log.info("Getting an overview");
        return ResponseEntity.ok(jellyService.getJellyOverview());
    }

    @GetMapping("overview/cage")
    public ResponseEntity<CageOverviewListDTO> getCages(@RequestParam(name = "cageNumber", required = false) Long cageNumber) {
        List<CageOverviewDTO> result;
        if (cageNumber == null) {
            log.info("Getting overview for all cages");
            result = jellyService.getCageOverview();
        } else {
            log.info("Getting overview for cage: {}", cageNumber);
            result = jellyService.getCageOverview(cageNumber);
        }
        return ResponseEntity.ok(new CageOverviewListDTO(result));
    }
}
