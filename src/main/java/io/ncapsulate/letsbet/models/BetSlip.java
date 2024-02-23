package io.ncapsulate.letsbet.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "bet_slips")
public class BetSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "selected_bets", joinColumns = @JoinColumn(name = "bet_slip_id"))
    @Column(name = "selected_bet")
    private List<String> selectedBets;

    public BetSlip() {
    }

    public BetSlip(Long id, User user, List<String> selectedBets) {
        this.id = id;
        this.user = user;
        this.selectedBets = selectedBets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getSelectedBets() {
        return selectedBets;
    }

    public void setSelectedBets(List<String> selectedBets) {
        this.selectedBets = selectedBets;
    }
}
