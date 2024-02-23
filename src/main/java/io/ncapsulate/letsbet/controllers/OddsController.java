package io.ncapsulate.letsbet.controllers;

import io.ncapsulate.letsbet.payload.request.AuthenticationRequest;
import io.ncapsulate.letsbet.payload.request.LoginRequest;
import io.ncapsulate.letsbet.payload.response.UserInfoResponse;
import io.ncapsulate.letsbet.repository.RoleRepository;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.jwt.JwtUtils;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/odds")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OddsController {

    private final OddsService oddsService;

    public OddsController(OddsService oddsService) {
        this.oddsService = oddsService;
    }

    @GetMapping("/live")
    public String getLiveOdds() {
        return oddsService.getLiveOdds();

    }
}
