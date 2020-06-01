package org.meijer.jelly.jellyFarmService.controller;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewDTO;
import org.meijer.jelly.jellyFarmService.model.cage.dto.CageOverviewListDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Color;
import org.meijer.jelly.jellyFarmService.model.jelly.attributes.Gender;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyListDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyOverviewDTO;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/stock")
    public ResponseEntity<JellyListDTO> getStock(@RequestParam(name = "cageNumber", required = false) Long cageNumber,
                                                 @RequestParam(name = "color", required = false) Color color,
                                                 @RequestParam(name = "gender", required = false) Gender gender) {
        log.info("Getting list of jellies with filters: cageNumber: {}, color: {}, gender: {}",
                cageNumber,
                cageNumber,
                gender);
        List<JellyDTO> result = jellyService.getJellies(cageNumber, color, gender);
        return ResponseEntity.ok(new JellyListDTO(result));
    }

    @GetMapping("/stock/{uuid}")
    public ResponseEntity<JellyDTO> getJelly(@PathVariable(name = "uuid") UUID id) {
        log.info("Getting details for single jelly");
        return ResponseEntity.ok(jellyService.getJelly(id));
    }

    @GetMapping("/overview")
    public ResponseEntity<JellyOverviewDTO> getAllJellies() {
        log.info("Getting an overview for all jellies");
        return ResponseEntity.ok(jellyService.getJellyOverview());
    }

    @GetMapping("overview/cage")
    public ResponseEntity<CageOverviewListDTO> getCages(
            @RequestParam(name = "cageNumbers", required = false) List<Long> cageNumbers) {
        List<CageOverviewDTO> result;
        if (cageNumbers == null || cageNumbers.isEmpty()) {
            log.info("Getting overview for all cages");
            result = jellyService.getCageOverview();
        } else {
            log.info("Getting overview for cages: {}", cageNumbers);
            result = jellyService.getCageOverview(cageNumbers);
        }
        return ResponseEntity.ok(new CageOverviewListDTO(result));
    }
}
