package io.ncapsulate.letsbet.controllers;


import io.ncapsulate.letsbet.models.BetOption;
import io.ncapsulate.letsbet.models.BetSlip;
import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.payload.request.AuthenticationRequest;
import io.ncapsulate.letsbet.payload.request.BetSlipRequest;
import io.ncapsulate.letsbet.payload.response.UserInfoResponse;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.jwt.JwtUtils;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
                    betOption.setBetOptionId(selectedBet.getId());
                    betOption.setGameId(selectedBet.getGameId());
                    betOption.setOutcome(selectedBet.getOutcome());
                    betOption.setPoint(selectedBet.getPoint());
                    betOption.setHomeTeam(selectedBet.getHomeTeam());
                    betOption.setAwayTeam(selectedBet.getAwayTeam());
                    return betOption;
                })
                .collect(Collectors.toSet());

        betSlipService.saveBetSlip(betSlipRequest.getUsername(), selectedBets);

//        betSlipService.saveBetSlip(betSlipRequest.getUsername(), betSlipRequest.getSelectedBets());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));

    }

//    @GetMapping("/saved")
//    public ResponseEntity<List<BetSlip>> getUserBetSlips(HttpServletRequest request) {
//        // Retrieve the user's bet slips
//        String jwt = jwtUtils.getJwtFromCookies(request);
//        String username = jwtUtils.getUserNameFromJwtToken(jwt);
//
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
//
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//
//        List<BetSlip> betSlips = betSlipService.getUserBetSlips(username);
//
//        if (betSlips.isEmpty()) {
//            return ResponseEntity.noContent().build(); // Return 204 No Content if no bet slips found
//        } else {
//            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt)
//                    .body(betSlips);
//        }
//    }

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

//    @GetMapping("/saved")
//    public List<BetSlip> getUserBetSlips(@RequestParam("username") String username) {
//
//        List<BetSlip> betSlips = betSlipService.getUserBetSlips(username);
//
//        return betSlips;
//
//    }

}
