package io.ncapsulate.letsbet.repository;

import io.ncapsulate.letsbet.models.Matchup;
import io.ncapsulate.letsbet.models.MatchupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchupRepository extends JpaRepository<Matchup, Long> {

    Matchup findMatchupById (Long id);

    @Query("SELECT m FROM Matchup m WHERE m.status = 'ACCEPTED'")
    List<Matchup> findAcceptedMatchups();

    @Query("SELECT m FROM Matchup m WHERE (m.user2.id = :id) AND m.status = 'PENDING'")
    List<Matchup> findPendingMatchupRequestsByUserId(@Param("id") Long id);

    @Query("SELECT m FROM Matchup m WHERE (m.user1.id = :userId OR m.user2.id = :userId) AND m.status = :status")
    List<Matchup> findActiveMatchupsByUserId(@Param("userId") Long userId, @Param("status") MatchupStatus status);

    @Query("SELECT m FROM Matchup m WHERE (m.user1.id = :userId OR m.user2.id = :userId) AND m.status = :status")
    List<Matchup> findCompletedMatchupsByUserId(@Param("userId") Long userId, @Param("status") MatchupStatus status);
}
