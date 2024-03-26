package io.ncapsulate.letsbet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;



/**
 * Represents the User entity with information such as username, email, password, and roles.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the user.

    @NotBlank
    @Size(max = 20)
    private String username; // User's username.

    @NotBlank
    @Size(max = 50)
    @Email
    private String email; // User's email address.

    @NotBlank
    @Size(max = 120)
    private String password; // User's password.

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>(); // Set of roles associated with the user.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<BetSlip> betSlips = new HashSet<>();


    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String username, String email, String password, Set<BetSlip> betSlips) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.betSlips = betSlips;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<BetSlip> getBetSlips() {
        return betSlips;
    }

    public void setBetSlips(Set<BetSlip> betSlips) {
        this.betSlips = betSlips;
    }

    public void addBetSlip(BetSlip betSlip) {
        betSlips.add(betSlip);
        betSlip.setUser(this);
    }

}
