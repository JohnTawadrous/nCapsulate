package io.ncapsulate.letsbet.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private BetSlip betSlip;
    private String outcome;
    private int point;

    @Column(name = "home_team")
    private String homeTeam;

    @Column(name = "away_team")
    private String awayTeam;

    private boolean isCorrect;

    private boolean isCompleted;

    @Enumerated(EnumType.STRING)
    private BetType betType;

    private String marketKey;


    public BetOption(){

    }

    public BetOption(Long id, String gameId, BetSlip betSlip, String outcome, int point, String homeTeam, String awayTeam, String marketKey) {
        this.id = id;
        this.gameId = gameId;
        this.betSlip = betSlip;
        this.outcome = outcome;
        this.point = point;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.marketKey = marketKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
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

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public BetType getBetType() {
        return betType;
    }

    public void setBetType(BetType betType) {
        this.betType = betType;
    }

    public String getMarketKey() {
        return marketKey;
    }

    public void setMarketKey(String marketKey) {
        this.marketKey = marketKey;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
