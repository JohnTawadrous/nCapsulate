package io.ncapsulate.letsbet.controllers;

import io.ncapsulate.letsbet.services.OddsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/odds")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OddsController {

    private final OddsService oddsService;

    public OddsController(OddsService oddsService) {
        this.oddsService = oddsService;
    }

    @GetMapping("/live")
    public String getLiveOdds() {
        return oddsService.getLiveOdds();

    }
}
