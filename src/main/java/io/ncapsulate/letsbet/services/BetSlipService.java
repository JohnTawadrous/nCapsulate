package io.ncapsulate.letsbet.services;

import io.ncapsulate.letsbet.models.BetOption;
import io.ncapsulate.letsbet.models.BetSlip;
import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.repository.BetSlipRepository;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
public class BetSlipService {

    @Autowired
    private BetSlipRepository betSlipRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private OddsService oddsService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveBetSlip(String username, Set<BetOption> selectedBets) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // Create a new bet slip entity
        BetSlip betSlip = new BetSlip();

        betSlip.setUser(user);

        // Set selected bets
        betSlip.setSelectedBets(selectedBets);

        // Save the bet slip
        betSlipRepository.save(betSlip);
    }

    public List<BetSlip> getUserBetSlips(String username){

        List<BetSlip> betSlips = betSlipRepository.findBetSlipByUsername(username);

        return betSlips;
    }

    public BetSlip getBetSlip(Long id){
        BetSlip betSlip = betSlipRepository.findBetSlipById(id);

        return betSlip;
    }

    @Transactional(readOnly = true)
    public List<BetSlip> getUserBetSlipsForToday(String username) {
        Date today = new Date();
        return betSlipRepository.findByUsernameAndCreatedAt(username, today);
    }

    public void updateTotalCorrectBetsForBetSlip(BetSlip betSlip) {
        int totalCorrectBets = 0;
        Set<BetOption> selectedBets = betSlip.getSelectedBets();
        for (BetOption betOption : selectedBets) {
            if (betOption.isCorrect()) {
                totalCorrectBets++;
            }
        }
        betSlip.setTotalCorrectBets(totalCorrectBets);
        betSlipRepository.save(betSlip);
    }

    public List<BetSlip> getAllBetSlips() {
        return betSlipRepository.findAll();
    }

    public void updateCompletedForBetSlip(BetSlip betSlip){
        boolean isCompleted = true;
        Set<BetOption> selectedBets = betSlip.getSelectedBets();
        for (BetOption betOption : selectedBets) {
            if (!betOption.isCompleted()) {
                isCompleted = false;
                break;
            }
        }
        betSlip.setIsCompleted(isCompleted);
        betSlipRepository.save(betSlip);
    }

    public boolean areAllGamesCompleted(BetSlip betSlip) {
        if (betSlip == null) {
            return false;
        }
        betSlip.setIsCompleted(betSlip.getIsCompleted());
        betSlipRepository.save(betSlip);
        return betSlip.getIsCompleted();
    }
}
