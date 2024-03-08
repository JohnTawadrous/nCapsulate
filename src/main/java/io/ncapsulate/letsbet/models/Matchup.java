package io.ncapsulate.letsbet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "matchups")
public class Matchup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "betslip_user1_id", referencedColumnName = "id")
    @JsonIgnore
    private BetSlip betSlipUser1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "betslip_user2_id", referencedColumnName = "id")
    @JsonIgnore
    private BetSlip betSlipUser2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    @JsonIgnore
    private User winner;

    @Enumerated(EnumType.STRING)
    private MatchupStatus status;

    private int user1CorrectBets;

    private int user2CorrectBets;

    public Matchup(){}

    public Matchup(Long id, User user1, User user2, BetSlip betSlipUser1, BetSlip betSlipUser2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.betSlipUser1 = betSlipUser1;
        this.betSlipUser2 = betSlipUser2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public BetSlip getBetSlipUser1() {
        return betSlipUser1;
    }

    public void setBetSlipUser1(BetSlip betSlipUser1) {
        this.betSlipUser1 = betSlipUser1;
    }

    public BetSlip getBetSlipUser2() {
        return betSlipUser2;
    }

    public void setBetSlipUser2(BetSlip betSlipUser2) {
        this.betSlipUser2 = betSlipUser2;
    }

    public int getUser1CorrectBets() {
        return user1CorrectBets;
    }

    public void setUser1CorrectBets(int user1CorrectBets) {
        this.user1CorrectBets = user1CorrectBets;
    }

    public int getUser2CorrectBets() {
        return user2CorrectBets;
    }

    public void setUser2CorrectBets(int user2CorrectBets) {
        this.user2CorrectBets = user2CorrectBets;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public MatchupStatus getStatus() {
        return status;
    }

    public void setStatus(MatchupStatus status) {
        this.status = status;
    }
}
