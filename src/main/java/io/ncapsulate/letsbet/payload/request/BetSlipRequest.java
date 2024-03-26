package io.ncapsulate.letsbet.payload.request;

import io.ncapsulate.letsbet.models.BetOption;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public class BetSlipRequest {

    @NotBlank
    private String username;
    @NotBlank
    private Set<BetOption> selectedBets;

    // Constructors, getters, and setters

    public BetSlipRequest() {
    }

    public BetSlipRequest(String username, Set<BetOption> selectedBets) {
        this.username = username;
        this.selectedBets = selectedBets;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<BetOption> getSelectedBets() {
        return selectedBets;
    }

    public void setSelectedBets(Set<BetOption> selectedBets) {
        this.selectedBets = selectedBets;
    }

}
