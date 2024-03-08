package io.ncapsulate.letsbet.services;

import io.ncapsulate.letsbet.models.BetOption;
import io.ncapsulate.letsbet.models.BetSlip;
import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.repository.BetSlipRepository;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import io.ncapsulate.letsbet.services.OddsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        betSlip.setUsername(userDetails);

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
}
