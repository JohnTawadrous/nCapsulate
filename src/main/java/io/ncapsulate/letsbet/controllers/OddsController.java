package io.ncapsulate.letsbet.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
