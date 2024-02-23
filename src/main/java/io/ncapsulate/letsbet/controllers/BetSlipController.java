package io.ncapsulate.letsbet.controllers;


import io.ncapsulate.letsbet.payload.request.AuthenticationRequest;
import io.ncapsulate.letsbet.payload.request.BetSlipRequest;
import io.ncapsulate.letsbet.payload.response.UserInfoResponse;
import io.ncapsulate.letsbet.security.jwt.JwtUtils;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/save")
    public ResponseEntity<?> saveBetSlip(@Valid @RequestBody BetSlipRequest betSlipRequest){

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(betSlipRequest.getUsername());

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        betSlipService.saveBetSlip(betSlipRequest.getUsername(), betSlipRequest.getSelectedBets());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));

    }

}
