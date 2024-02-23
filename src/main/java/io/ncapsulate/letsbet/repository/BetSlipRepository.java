package io.ncapsulate.letsbet.repository;

import io.ncapsulate.letsbet.models.BetSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetSlipRepository extends JpaRepository<BetSlip, Long> {
    // You can define custom query methods here if needed
    List<BetSlip> findByUserUsername(String username);
}
