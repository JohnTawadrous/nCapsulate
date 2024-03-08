package io.ncapsulate.letsbet.controllers;

import io.ncapsulate.letsbet.models.GameScore;
import io.ncapsulate.letsbet.services.BetOptionService;
import io.ncapsulate.letsbet.services.OddsService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private OddsService oddsService;

    @Autowired
    private BetOptionService betOptionService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(cron = "36 12 * * * *", zone = "America/New_York")
    public void fetchGameScoresDaily() {
        // Call your method to fetch game scores here
        logger.info("Scheduled task to fetch game scores started.");
        try {
            List<GameScore> gameScores = oddsService.fetchGameScores();
            logger.info("fetchGameScores method complete");
            betOptionService.updateBetOptionsWithGameScores(gameScores);
            logger.info("updateBetOptionsWithGameScores method complete");
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
