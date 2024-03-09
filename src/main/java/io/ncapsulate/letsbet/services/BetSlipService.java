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


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<BetSlip> getUserBetSlipsForToday(String username) {
        Date today = new Date();
        return betSlipRepository.findByUsernameAndCreatedAt(username, today);
    }

//    @Transactional(readOnly = true)
//    public List<BetSlip> getUserBetSlipsForToday(String username) {
//        // Get the user object from the username
//        User user = userRepository.findUserByUsername(username);
////                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//
//        // Get the start of the current day
//        Date startOfDay = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
//
//        // Retrieve the bet slips for today
//        return betSlipRepository.findByUserAndCreatedAt(user, startOfDay);
//    }

//    private List<BetSlip> filterBetSlipsByCurrentDay(List<BetSlip> betSlips) {
//        // Get the current date
//        Calendar currentCal = Calendar.getInstance();
//        currentCal.setTime(new Date());
//        int currentDayOfYear = currentCal.get(Calendar.DAY_OF_YEAR);
//        int currentYear = currentCal.get(Calendar.YEAR);
//
//        // Filter bet slips created on the current day
//        return betSlips.stream()
//                .filter(betSlip -> {
//                    Calendar betSlipCal = Calendar.getInstance();
//                    betSlipCal.setTime(betSlip.getCreatedAt());
//                    int betSlipDayOfYear = betSlipCal.get(Calendar.DAY_OF_YEAR);
//                    int betSlipYear = betSlipCal.get(Calendar.YEAR);
//                    return betSlipDayOfYear == currentDayOfYear && betSlipYear == currentYear;
//                })
//                .collect(Collectors.toList());
//    }
}
