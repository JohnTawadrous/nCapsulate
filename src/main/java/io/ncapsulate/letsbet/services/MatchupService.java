package io.ncapsulate.letsbet.services;

import io.ncapsulate.letsbet.models.BetSlip;
import io.ncapsulate.letsbet.models.Matchup;
import io.ncapsulate.letsbet.models.MatchupStatus;
import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.payload.request.MatchupRequest;
import io.ncapsulate.letsbet.repository.BetSlipRepository;
import io.ncapsulate.letsbet.repository.MatchupRepository;
import io.ncapsulate.letsbet.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchupService {

    private final UserRepository userRepository;

    private final MatchupRepository matchupRepository;
    private final BetOptionService betOptionService;

    private final BetSlipRepository betSlipRepository;

    public MatchupService(MatchupRepository matchupRepository, UserRepository userRepository, BetOptionService betOptionService, BetSlipRepository betSlipRepository) {
        this.matchupRepository = matchupRepository;
        this.userRepository = userRepository;
        this.betOptionService = betOptionService;
        this.betSlipRepository = betSlipRepository;
    }

    // Method to create a new matchup
    public Matchup createMatchup(User user1, User user2, BetSlip betSlipUser1, BetSlip betSlipUser2) {
        Matchup matchup = new Matchup();
        matchup.setUser1(user1);
        matchup.setUser2(user2);
        matchup.setBetSlipUser1(betSlipUser1);
        matchup.setBetSlipUser2(betSlipUser2);
        return matchupRepository.save(matchup);
    }


    //Method to update Matchup with Results
    public void updateMatchupResults(Matchup matchup){

        // Calculate total correct bets for each user's bet slip
        int user1CorrectBets = matchup.getBetSlipUser1().getTotalCorrectBets();
        int user2CorrectBets = matchup.getBetSlipUser2().getTotalCorrectBets();


        // Set the correct bets count in the matchup entity
        matchup.setUser1CorrectBets(user1CorrectBets);
        matchup.setUser2CorrectBets(user2CorrectBets);

        // Determine the winner based on correct bets count
        if (user1CorrectBets > user2CorrectBets) {
            matchup.setWinner(matchup.getUser1());
        } else if (user1CorrectBets < user2CorrectBets) {
            matchup.setWinner(matchup.getUser2());
        } else {
            // Handle tie if necessary
            matchup.setWinner(null); // or set to a special value indicating a tie
        }

        // Save or update the matchup entity in the repository
        matchupRepository.save(matchup);
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

    public void acceptMatchup(Long matchupId) throws ChangeSetPersister.NotFoundException {
        Matchup matchup = matchupRepository.findById(matchupId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        matchup.setStatus(MatchupStatus.ACCEPTED);
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

}
