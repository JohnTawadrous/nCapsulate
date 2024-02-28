package io.ncapsulate.letsbet.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bet_slips")
public class BetSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "betSlip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BetOption> selectedBets = new HashSet<>();

    public BetSlip() {
    }

    public BetSlip(Long id, User user, Set<BetOption> selectedBets) {
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

    public Set<BetOption> getSelectedBets() {
        return selectedBets;
    }

    public void setSelectedBets(Set<BetOption> selectedBets) {
        this.selectedBets = selectedBets;
    }
}
