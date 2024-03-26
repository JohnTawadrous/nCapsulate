package io.ncapsulate.letsbet.services;

import io.ncapsulate.letsbet.models.*;
import io.ncapsulate.letsbet.repository.BetOptionRepository;
import io.ncapsulate.letsbet.repository.BetSlipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BetOptionService {

    @Autowired
    private BetOptionRepository betOptionRepository;

    @Autowired
    private BetSlipRepository betSlipRepository;

    private static final Logger logger = LoggerFactory.getLogger(BetOptionService.class);

    // Method to update bet options
    @Transactional
    public void updateBetOptions(List<BetOption> betOptions) {
        // Iterate through each bet option and update it in the database
        for (BetOption betOption : betOptions) {
            try {
                // Save the updated bet option in the database
                betOptionRepository.save(betOption);
                logger.info("Bet option updated: {}", betOption);

                // Retrieve the associated bet slip and update its total correct bets
                BetSlip betSlip = betOption.getBetSlip();
                if (betSlip != null) {
                    updateBetSlipTotalCorrectBets(betSlip);
                }
            } catch (Exception e) {
                logger.error("Error updating bet option: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void updateBetOptionsWithGameScores(List<GameScore> gameScores) {


        for (GameScore gameScore : gameScores) {

            if (gameScore == null) {
                // Log or handle the null gameScore object
                continue; // Skip processing if the gameScore is null
            }

            // Retrieve bet options associated with the game ID
            List<BetOption> betOptions = betOptionRepository.findBetOptionsByGameId(gameScore.getId());
            logger.info("Fetched {} bet options for game ID: {}", betOptions.size(), gameScore.getId());

            // Check if the game is completed and update the isCompleted field for associated bet options
            boolean isGameCompleted = gameScore.isCompleted();

            // Compare game score with bet options and update isCorrect field
            for (BetOption betOption : betOptions) {
                betOption.setCompleted(isGameCompleted);
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

                    boolean predictedHomeTeam = predictedTeam.equals(gameScore.getScoreName(0));

                    if(predictedHomeTeam){
                        isCorrect = -predictedSpread <= actualSpread;
                    }
                    else{
                        isCorrect = -predictedSpread <= -actualSpread;
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

    @Transactional
    private void updateBetSlipTotalCorrectBets(BetSlip betSlip) {
        int totalCorrectBets = (int) betSlip.getSelectedBets().stream().filter(BetOption::isCorrect).count();
        betSlip.setTotalCorrectBets(totalCorrectBets);
        betSlipRepository.save(betSlip);
        logger.info("Bet slip updated with total correct bets: {}", totalCorrectBets);
    }
}
