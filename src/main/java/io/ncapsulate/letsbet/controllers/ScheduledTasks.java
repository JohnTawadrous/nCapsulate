package io.ncapsulate.letsbet.controllers;

import io.ncapsulate.letsbet.models.GameScore;
import io.ncapsulate.letsbet.services.BetOptionService;
import io.ncapsulate.letsbet.services.BetSlipService;
import io.ncapsulate.letsbet.services.MatchupService;
import io.ncapsulate.letsbet.services.OddsService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import io.ncapsulate.letsbet.models.BetSlip;

import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private OddsService oddsService;

    @Autowired
    private BetOptionService betOptionService;

    @Autowired
    private BetSlipService betSlipService;

    @Autowired
    private MatchupService matchupService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(cron = "36 12 * * * *", zone = "America/New_York")
    public void fetchGameScoresDaily() {
        // Call your method to fetch game scores here
        logger.info("Scheduled task to fetch game scores started.");
        try {

            List<BetSlip> betSlips = betSlipService.getAllBetSlips();
            for (BetSlip betSlip : betSlips) {
                betSlipService.updateCompletedForBetSlip(betSlip);
                betSlipService.updateTotalCorrectBetsForBetSlip(betSlip);

            }
            matchupService.updateMatchupResults();

            logger.info("updated matchup results");
            List<GameScore> gameScores = oddsService.fetchGameScores();
            logger.info("fetchGameScores method complete");
            betOptionService.updateBetOptionsWithGameScores(gameScores);
            logger.info("updateBetOptionsWithGameScores method complete");




            matchupService.updateMatchupResults();

        } catch (Exception e) {
            // Handle exceptions
            logger.error("Error occurred during scheduled task execution: {}", e.getMessage());
            e.printStackTrace();
        }

        logger.info("Scheduled task to fetch game scores completed.");
    }

    @PostConstruct
    public void executeScheduledTaskManually() {
        fetchGameScoresDaily();
    }
}
