package org.meijer.jelly.jellyFarmService.controller;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.model.adoption.AdoptionRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.FreeJellyRequestDTO;
import org.meijer.jelly.jellyFarmService.model.adoption.RecageRequestDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyDTO;
import org.meijer.jelly.jellyFarmService.model.jelly.dto.JellyListDTO;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/adoption")
@Slf4j
public class JellyAdoptionController {
    private final JellyService jellyService;

    @Autowired
    public JellyAdoptionController(JellyService jellyService) {
        this.jellyService = jellyService;
    }

    @PostMapping("/adopt")
    public ResponseEntity<JellyDTO> adoptJelly(@RequestBody @Valid AdoptionRequestDTO adoptionRequest) {
        log.info("Request for adoption received");
        return ResponseEntity.ok(jellyService.adoptJelly(adoptionRequest));
    }

    @PutMapping("/recage")
    public ResponseEntity<JellyListDTO> recageJellies(@RequestBody @Valid RecageRequestDTO recageRequestDTO) {
        log.info("Request for recaging jellies received");
        return ResponseEntity.ok(new JellyListDTO(jellyService.recageJellies(recageRequestDTO)));
    }

    @DeleteMapping("/free")
    public ResponseEntity<JellyListDTO> freeJellies(@RequestBody @Valid FreeJellyRequestDTO freeJellyRequestDTO) {
        log.info("Request for recaging jellies received");
        return ResponseEntity.ok(new JellyListDTO(jellyService.freeJellies(freeJellyRequestDTO)));
    }



}
