package io.ncapsulate.letsbet.repository;

import io.ncapsulate.letsbet.models.BetOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetOptionRepository extends JpaRepository<BetOption, Long> {

    public List<BetOption> findBetOptionsByGameId(String gameId);



}
