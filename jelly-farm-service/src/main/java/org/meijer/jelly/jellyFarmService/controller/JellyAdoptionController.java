package org.meijer.jelly.jellyFarmService.controller;

import lombok.extern.slf4j.Slf4j;
import org.meijer.jelly.jellyFarmService.service.JellyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/adoption")
@Slf4j
public class JellyAdoptionController {
    private JellyService jellyService;

    @Autowired
    public JellyAdoptionController(JellyService jellyService) {
        this.jellyService = jellyService;
    }

}
