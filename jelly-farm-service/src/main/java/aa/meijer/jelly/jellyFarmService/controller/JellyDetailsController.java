package aa.meijer.jelly.jellyFarmService.controller;

import aa.meijer.jelly.jellyFarmService.model.jelly.Jelly;
import aa.meijer.jelly.jellyFarmService.service.JellyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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
    public ResponseEntity<List<Jelly>> getStock(@RequestParam(name = "cageNumber", required = false) Long cageNumber) {
        List<Jelly> response;
        if (cageNumber == null) {
            log.info("Received request for list of all jellies");
            response = jellyService.getAllJellies();
        } else {
            log.info("Received request for list of all jellies in cage number {}", cageNumber);
            response = jellyService.getJelliesByCage(cageNumber);
        }
        return ResponseEntity.ok(response);
    }
}
