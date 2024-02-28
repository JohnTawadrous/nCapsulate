package io.ncapsulate.letsbet.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ncapsulate.letsbet.models.BetOption;
import io.ncapsulate.letsbet.models.BetSlip;
import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.repository.BetSlipRepository;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BetSlipService {

    @Autowired
    private BetSlipRepository betSlipRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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

//    public List<BetSlip> getUserBetSlips(String username) throws UsernameNotFoundException{
//        // Find the user by username
//        Optional<User> optionalUser = userRepository.findByUsername(username);
//        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
////                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        // Retrieve the user's bet slips
//        List<BetSlip> betSlips = user.getBetSlips();
//
//        return betSlips;
//    }


    public List<BetSlip> getUserBetSlips(String username){

        List<BetSlip> betSlips = betSlipRepository.findByUserUsername(username);

        return betSlips;
    }
}
