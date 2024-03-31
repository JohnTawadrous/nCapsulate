package io.ncapsulate.letsbet.services;

import io.ncapsulate.letsbet.models.*;
import io.ncapsulate.letsbet.payload.request.MatchupRequest;
import io.ncapsulate.letsbet.repository.BetSlipRepository;
import io.ncapsulate.letsbet.repository.MatchupRepository;
import io.ncapsulate.letsbet.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchupRepository matchupRepository;
    @Autowired
    private BetOptionService betOptionService;
    @Autowired
    private OddsService oddsService;

    @Autowired
    private BetSlipService betSlipService;

    @Autowired
    private BetSlipRepository betSlipRepository;

    private static final Logger logger = LoggerFactory.getLogger(MatchupService.class);

    // Method to create a new matchup
    public Matchup createMatchup(User user1, User user2, BetSlip betSlipUser1, BetSlip betSlipUser2) {
        Matchup matchup = new Matchup();
        matchup.setUser1(user1);
        matchup.setUser2(user2);
        matchup.setBetSlipUser1(betSlipUser1);
        matchup.setBetSlipUser2(betSlipUser2);
        return matchupRepository.save(matchup);
    }

    private BetSlip initializeBetSlip(BetSlip betSlip) {
        if (!Hibernate.isInitialized(betSlip)) {
            Hibernate.initialize(betSlip);
        }
        return betSlip;
    }


    //Method to update Matchup with Results
    @Transactional
    public void updateMatchupResult(Matchup matchup){

        // Initialize the proxy objects to avoid LazyInitializationException
        BetSlip betSlipUser1 = initializeBetSlip(matchup.getBetSlipUser1());
        BetSlip betSlipUser2 = initializeBetSlip(matchup.getBetSlipUser2());

        // Calculate total correct bets for each user's bet slip
        int user1CorrectBets = betSlipUser1.getTotalCorrectBets();
        int user2CorrectBets = betSlipUser2.getTotalCorrectBets();

        // Set the correct bets count in the matchup entity
        matchup.setUser1CorrectBets(user1CorrectBets);
        matchup.setUser2CorrectBets(user2CorrectBets);

        // Check if all games are completed for both bet slips
        boolean isBetSlipUser1Completed = betSlipUser1.getIsCompleted();
        boolean isBetSlipUser2Completed = betSlipUser2.getIsCompleted();

        // If both bet slips have all games completed, set matchup status to completed and determine the winner
        if (isBetSlipUser1Completed && isBetSlipUser2Completed) {
            // Determine the winner based on correct bets count
            if (user1CorrectBets > user2CorrectBets) {
                matchup.setWinner(matchup.getUser1());
            } else if (user1CorrectBets < user2CorrectBets) {
                matchup.setWinner(matchup.getUser2());
            } else {
                // Handle tie if necessary
                matchup.setWinner(null); // or set to a special value indicating a tie
            }
            matchup.setStatus(MatchupStatus.COMPLETED);
        } else {
            // If not all games are completed, keep the status unchanged or handle other transitions as needed
            matchup.setWinner(null);
            logger.info("Not all games completed yet");
        }

        // Save or update the matchup entity in the repository
        try {
            matchupRepository.save(matchup);
        } catch (DataIntegrityViolationException e) {

            logger.error("Error saving matchup:", e);
        }
    }

    public void updateMatchupResults() {
        List<Matchup> acceptedMatchups = matchupRepository.findAcceptedMatchups();
        for (Matchup matchup : acceptedMatchups) {
            updateMatchupResult(matchup);
        }
    }

    //Method to send Matchup Request
    public void sendMatchupRequest(MatchupRequest matchupRequest){

        //Get current User
        User currentUser = userRepository.findUserByUsername(matchupRequest.getUserUsername());
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        //Get Opponent User
        User opponentUser = userRepository.findUserByUsername(matchupRequest.getOpponentUsername());
        if (opponentUser == null) {
            throw new RuntimeException("Opponent user not found");
        }

        Long selectedBetSlipId = matchupRequest.getSelectedBetSlipId();
        BetSlip selectedBetSlip = betSlipRepository.findBetSlipById(selectedBetSlipId);

        // Create a new matchup
        Matchup matchup = createMatchup(currentUser, opponentUser, selectedBetSlip, null); // Assuming opponent's bet slip is not specified yet

        matchup.setStatus(MatchupStatus.PENDING);

        // Save the matchup entity in the repository
        matchupRepository.save(matchup);

    }

    public void acceptMatchup(Long matchupId, Long selectedBetSlipId) throws ChangeSetPersister.NotFoundException {
        Matchup matchup = matchupRepository.findById(matchupId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        matchup.setStatus(MatchupStatus.ACCEPTED);
        BetSlip selectedBetSlip = betSlipRepository.findBetSlipById(selectedBetSlipId);
        matchup.setBetSlipUser2(selectedBetSlip);
        matchupRepository.save(matchup);
    }

    public void declineMatchup(Long matchupId) throws ChangeSetPersister.NotFoundException {
        Matchup matchup = matchupRepository.findById(matchupId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        matchup.setStatus(MatchupStatus.DECLINED);
        matchupRepository.save(matchup);
    }

    public List<Matchup> getPendingMatchupRequestsForUser(Long id) {
        return matchupRepository.findPendingMatchupRequestsByUserId(id);
    }

    public String getUsernameById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return user.getUsername();
    }

    public List<Matchup> getActiveMatchups(Long userId) {
        List<Matchup> activeMatchups = matchupRepository.findActiveMatchupsByUserId(userId, MatchupStatus.ACCEPTED);

        return activeMatchups;
    }
    public List<Matchup> getCompletedMatchups(Long userId) {
        return matchupRepository.findCompletedMatchupsByUserId(userId, MatchupStatus.COMPLETED);
    }

}
