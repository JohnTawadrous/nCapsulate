package io.ncapsulate.letsbet.repository;

import io.ncapsulate.letsbet.models.BetSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetSlipRepository extends JpaRepository<BetSlip, Long> {

    @Query("SELECT bs FROM BetSlip bs JOIN FETCH bs.selectedBets WHERE bs.user.username = :username")
    List<BetSlip> findBetSlipByUsername(@Param("username") String username);
}
