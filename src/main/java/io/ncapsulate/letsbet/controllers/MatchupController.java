package io.ncapsulate.letsbet.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ncapsulate.letsbet.models.Matchup;
import io.ncapsulate.letsbet.payload.request.MatchupRequest;
import io.ncapsulate.letsbet.repository.MatchupRepository;
import io.ncapsulate.letsbet.security.jwt.JwtUtils;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import io.ncapsulate.letsbet.services.MatchupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@RestController
@RequestMapping("/api/matchups")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MatchupController {

    private final MatchupService matchupService;

    private final MatchupRepository matchupRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    public MatchupController(MatchupService matchupService, MatchupRepository matchupRepository) {
        this.matchupService = matchupService;
        this.matchupRepository = matchupRepository;
    }

    @PostMapping("/send-request")
    public ResponseEntity<?> sendMatchupRequest(@RequestBody MatchupRequest matchupRequest) {
        try {
            matchupService.sendMatchupRequest(matchupRequest);
            return ResponseEntity.ok("Matchup request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send matchup request: " + e.getMessage());
        }
    }

    @PostMapping("/accept/{matchupId}")
    public ResponseEntity<?> acceptMatchup(@PathVariable Long matchupId) {
        try {
            matchupService.acceptMatchup(matchupId);
            return ResponseEntity.ok("Matchup accepted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to accept matchup: " + e.getMessage());
        }
    }

    @PostMapping("/decline/{matchupId}")
    public ResponseEntity<?> declineMatchup(@PathVariable Long matchupId) {
        try {
            matchupService.declineMatchup(matchupId);
            return ResponseEntity.ok("Matchup declined successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decline matchup: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Matchup>> getPendingMatchupRequests(@RequestParam("username") String username) {

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        Long userId = userDetails.getId();

        List<Matchup> pendingMatchups = matchupService.getPendingMatchupRequestsForUser(userId);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(pendingMatchups);
    }

    @GetMapping("/{userId}/username")
    public ResponseEntity<String> getUsernameById(@PathVariable Long userId) {
        String username = matchupService.getUsernameById(userId);
        return ResponseEntity.ok(username);
    }

}
