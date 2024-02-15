package io.ncapsulate.letsbet.controllers;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Service
public class OddsService {

    @Value("${odds.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OddsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLiveOdds() {
        String apiUrl = "https://api.the-odds-api.com/v4/sports/basketball_nba/odds/?apiKey=" + apiKey + "&regions=us&markets=spreads,totals&oddsFormat=decimal&bookmakers=draftkings";
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
