package aa.meijer.jellyfarm.controller;

import aa.meijer.jellyfarm.model.cage.CageOverview;
import aa.meijer.jellyfarm.model.jelly.Jelly;
import aa.meijer.jellyfarm.model.jelly.JellyOverview;
import aa.meijer.jellyfarm.model.jelly.attributes.Color;
import aa.meijer.jellyfarm.model.jelly.attributes.Gender;
import aa.meijer.jellyfarm.repository.entity.JellyEntity;
import aa.meijer.jellyfarm.service.JellyDetailsService;
import aa.meijer.jellyfarm.service.JellyOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@Slf4j
public class JellyDetailsController {
    private JellyDetailsService jellyDetailsService;
    private JellyOrderService jellyOrderService;

    @Autowired
    public JellyDetailsController(JellyDetailsService jellyDetailsService, JellyOrderService jellyOrderService) {
        this.jellyDetailsService = jellyDetailsService;
        this.jellyOrderService = jellyOrderService;
    }

    @GetMapping("/get")
    public ResponseEntity<JellyEntity> getJelly(@RequestParam(name = "uuid") UUID id) {
        log.info("Getting details for single jelly");
        return ResponseEntity.ok(jellyDetailsService.getJelly(id));
    }

    @GetMapping("/overview")
    public ResponseEntity<JellyOverview> getAllJellies() {
        log.info("Getting an overview");
        return ResponseEntity.ok(jellyDetailsService.getJellyOverview());
    }

    @PostMapping("/buy")
    public ResponseEntity buyJelly(@RequestParam(name = "color") Color color,
                                   @RequestParam(name = "gender") Gender gender,
                                   @RequestParam(name = "cageNumber") int cageNumber) {
        log.info("Buying a new jelly from the jelly salesman and adding it to our farm");
        jellyDetailsService.buyNewJelly(color, gender, cageNumber);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/sales")
    public ResponseEntity<Jelly> sellJelly(@RequestParam(name = "uuid") UUID id) {
        log.info("We sold a jelly! For money! Yay");
        return ResponseEntity.ok(jellyDetailsService.sellJelly(id));
    }

    @PostMapping("/order")
    public ResponseEntity placeOrder() {
        log.info("placing an order");
        jellyOrderService.testKafka();
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity deleteAll() {
        jellyDetailsService.deleteAll();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/cage")
    public ResponseEntity<CageOverview> getCages() {
        return new ResponseEntity(jellyDetailsService.getCageOverview(), HttpStatus.OK);
    }
}
