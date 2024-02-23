package io.ncapsulate.letsbet.payload.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class BetSlipRequest {

    @NotBlank
    private String username;
    @NotBlank
    private List<String> selectedBets;

    @NotBlank
    private String jwtToken;

    // Constructors, getters, and setters

    public BetSlipRequest() {
    }

    public BetSlipRequest(String username, List<String> selectedBets, String jwtToken) {
        this.username = username;
        this.selectedBets = selectedBets;
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getSelectedBets() {
        return selectedBets;
    }

    public void setSelectedBets(List<String> selectedBets) {
        this.selectedBets = selectedBets;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
