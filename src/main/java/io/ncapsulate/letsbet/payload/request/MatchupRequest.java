package io.ncapsulate.letsbet.payload.request;

public class MatchupRequest {

    private String userUsername;
    private String opponentUsername;
    private Long selectedBetSlipId;

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public Long getSelectedBetSlipId() {
        return selectedBetSlipId;
    }

    public void setSelectedBetSlipId(Long selectedBetSlipId) {
        this.selectedBetSlipId = selectedBetSlipId;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }
}
