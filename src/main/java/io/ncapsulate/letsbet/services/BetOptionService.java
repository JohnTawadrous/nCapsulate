package io.ncapsulate.letsbet.services;

import io.ncapsulate.letsbet.models.BetOption;
import io.ncapsulate.letsbet.models.BetType;
import io.ncapsulate.letsbet.models.GameScore;
import io.ncapsulate.letsbet.repository.BetOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetOptionService {

    @Autowired
    private BetOptionRepository betOptionRepository;

    private static final Logger logger = LoggerFactory.getLogger(BetOptionService.class);

    // Method to update bet options
    public void updateBetOptions(List<BetOption> betOptions) {
        // Iterate through each bet option and update it in the database
        for (BetOption betOption : betOptions) {
            try {
                // Save the updated bet option in the database
                betOptionRepository.save(betOption);
                logger.info("Bet option updated: {}", betOption);
            } catch (Exception e) {
                logger.error("Error updating bet option: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void updateBetOptionsWithGameScores(List<GameScore> gameScores) {


        for (GameScore gameScore : gameScores) {
            // Retrieve bet options associated with the game ID
            List<BetOption> betOptions = betOptionRepository.findBetOptionsByGameId(gameScore.getId());
            logger.info("Fetched {} bet options for game ID: {}", betOptions.size(), gameScore.getId());

            // Compare game score with bet options and update isCorrect field
            for (BetOption betOption : betOptions) {
                boolean isCorrect = false;
                logger.info("Processing bet option (id, type, outcome, point): {}, {}, {}, {}", betOption.getId(), betOption.getBetType(), betOption.getOutcome(), betOption.getPoint());
                logger.info("With gameScore id: {} and totalScore: {} and spread: {} ", gameScore.getId(), gameScore.getTotalScore(), gameScore.getSpread());



                if (betOption.getBetType() == BetType.OVER_UNDER ) {
                    // Compare total score with the predicted point
                    if (gameScore.getTotalScore() >= betOption.getPoint()) {
                        logger.info("game score : {}", gameScore.getTotalScore());
                        isCorrect = betOption.getOutcome().equals("Over");
                    } else {
                        isCorrect = betOption.getOutcome().equals("Under");
                        logger.info("game score : {}", gameScore.getTotalScore());
                    }
                } else if (betOption.getBetType() == BetType.SPREAD) {
                    // Compare spread with the predicted point
                    int actualSpread = gameScore.getSpread();
                    int predictedSpread = betOption.getPoint();
                    String predictedTeam = betOption.getOutcome();
                    // Determine if the predicted team is the home team
                    boolean predictedHomeTeam = predictedTeam.equals(gameScore.getHomeTeam());

                    // Adjust actual spread based on whether the predicted team is home or away
                    if (!predictedHomeTeam) {
                        actualSpread = -actualSpread; // Away team spread is negative
                    }

                    // Compare actual spread with predicted spread
                    if (actualSpread >= predictedSpread) {
                        isCorrect = true;
                    }
                }

                // Update isCorrect field
                betOption.setCorrect(isCorrect);
                logger.info("Bet option {} updated with isCorrect = {}", betOption.getId(), isCorrect);
            }

            // Update bet options in the database
            updateBetOptions(betOptions);

        }

    }
}
