package io.ncapsulate.letsbet.payload.response;

import java.util.List;

public class BetSlipResponse {

    private Long userId;
    private String username;
    private String email;
    private List<String> roles;
    private Long betSlipId;

    public BetSlipResponse(Long userId, String username, String email, List<String> roles, Long betSlipId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.betSlipId = betSlipId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getBetSlipId() {
        return betSlipId;
    }

    public void setBetSlipId(Long betSlipId) {
        this.betSlipId = betSlipId;
    }
}
