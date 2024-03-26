package io.ncapsulate.letsbet.controllers;


import io.ncapsulate.letsbet.models.BetOption;
import io.ncapsulate.letsbet.models.BetSlip;
import io.ncapsulate.letsbet.models.BetType;
import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.payload.request.BetSlipRequest;
import io.ncapsulate.letsbet.payload.response.BetSlipResponse;
import io.ncapsulate.letsbet.payload.response.UserInfoResponse;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.jwt.JwtUtils;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import io.ncapsulate.letsbet.services.BetSlipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/betslips")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BetSlipController {

    @Autowired
    private BetSlipService betSlipService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Value("${ncapsulate.app.jwtCookieName}")
    private String jwtCookie;


    @PostMapping("/save")
    public ResponseEntity<?> saveBetSlip(@Valid @RequestBody BetSlipRequest betSlipRequest){

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(betSlipRequest.getUsername());

        User user = userRepository.findUserByUsername(betSlipRequest.getUsername());

        BetSlip betSlip = new BetSlip();
        user.addBetSlip(betSlip);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Convert the selected bet strings to BetOption objects
        Set<BetOption> selectedBets = betSlipRequest.getSelectedBets().stream()
                .map(selectedBet -> {
                    BetOption betOption = new BetOption();
                    betOption.setBetSlip(betSlip);
                    betOption.setId(selectedBet.getId());
                    betOption.setGameId(selectedBet.getGameId());
                    betOption.setOutcome(selectedBet.getOutcome());
                    betOption.setPoint(selectedBet.getPoint());
                    betOption.setHomeTeam(selectedBet.getHomeTeam());
                    betOption.setAwayTeam(selectedBet.getAwayTeam());
                    betOption.setMarketKey(selectedBet.getMarketKey());
                    String marketKey = selectedBet.getMarketKey();
                    if ("spreads".equals(marketKey)) {
                        betOption.setBetType(BetType.SPREAD);
                    } else if ("totals".equals(marketKey)) {
                        betOption.setBetType(BetType.OVER_UNDER);
                    }
                    return betOption;
                })
                .collect(Collectors.toSet());

        betSlipService.saveBetSlip(betSlipRequest.getUsername(), selectedBets);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header("Bet-Slip-ID", String.valueOf(betSlip.getId()))
                .body(new BetSlipResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        betSlip.getId()
                        ));
    }

    @GetMapping("/saved")
    public ResponseEntity<List<BetSlip>> getUserBetSlips(@RequestParam("username") String username) {

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        List<BetSlip> betSlips = betSlipService.getUserBetSlips(username);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(betSlips);
    }

    @GetMapping("/saved-today")
    public ResponseEntity<List<BetSlip>> getUserBetSlipsForToday(@RequestParam("username") String username) {

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        List<BetSlip> betSlips = betSlipService.getUserBetSlipsForToday(username);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(betSlips);
    }

    @GetMapping("/betslip-details")
    public BetSlip getBetSlipById(@RequestParam("id") Long id){

        BetSlip betSlip = betSlipService.getBetSlip(id);
        return betSlip;
    }

}
