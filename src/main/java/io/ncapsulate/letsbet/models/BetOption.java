package io.ncapsulate.letsbet.models;

import jakarta.persistence.*;

@Entity
@Table(name = "selected_bets")
public class BetOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private String gameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_slip_id", nullable = false)
    private BetSlip betSlip;
    private String outcome;
    private String point;

    @Column(name = "home_team")
    private String homeTeam;

    @Column(name = "away_team")
    private String awayTeam;


    public BetOption(){

    }

    public BetOption(Long id, String gameId, BetSlip betSlip, String outcome, String point, String homeTeam, String awayTeam) {
        this.id = id;
        this.gameId = gameId;
        this.betSlip = betSlip;
        this.outcome = outcome;
        this.point = point;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Long getId() {
        return id;
    }

    public void setBetOptionId(Long betOptionId) {
        this.id = betOptionId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public BetSlip getBetSlip() {
        return betSlip;
    }

    public void setBetSlip(BetSlip betSlip) {
        this.betSlip = betSlip;
    }
}
