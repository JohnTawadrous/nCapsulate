package io.ncapsulate.letsbet.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bet_slips")
public class BetSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "betSlip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BetOption> selectedBets = new HashSet<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "betSlipUser1", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Matchup> matchupsForUser1 = new HashSet<>();

    @OneToMany(mappedBy = "betSlipUser2", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Matchup> matchupsForUser2 = new HashSet<>();

    private int totalCorrectBets;

    private boolean isCompleted;

    public BetSlip() {
    }

    public BetSlip(Long id, User user, Set<BetOption> selectedBets, Date createdAt) {
        this.id = id;
        this.user = user;
        this.selectedBets = selectedBets;
        this.createdAt = createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Matchup> getMatchupsForUser1() {
        return matchupsForUser1;
    }

    public void setMatchupsForUser1(Set<Matchup> matchupsForUser1) {
        this.matchupsForUser1 = matchupsForUser1;
    }

    public Set<Matchup> getMatchupsForUser2() {
        return matchupsForUser2;
    }

    public void setMatchupsForUser2(Set<Matchup> matchupsForUser2) {
        this.matchupsForUser2 = matchupsForUser2;
    }

    public int getTotalCorrectBets() {
        int totalCorrectBets = 0;
        for (BetOption betOption : selectedBets) {
            if (betOption.isCorrect()) {
                totalCorrectBets++;
            }
        }
        setTotalCorrectBets(totalCorrectBets);
        return totalCorrectBets;
    }

    public void setTotalCorrectBets(int totalCorrectBets){this.totalCorrectBets = totalCorrectBets;}

    public boolean getIsCompleted(){
        boolean isCompleted = true;
        for (BetOption betOption : selectedBets) {
            if (!betOption.isCompleted()) {
                isCompleted = false;
                break;
            }
        }
        setIsCompleted(isCompleted);
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
        isCompleted = completed;
    }

}
