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

    MatchupStatus findMatchupById (Long id);


//    @Query("SELECT * FROM lets_bet_db.matchups WHERE user1_id = :id OR user2_id = :id AND status = 'PENDING'")
@Query("SELECT m FROM Matchup m WHERE (m.user2.id = :id) AND m.status = 'PENDING'")
List<Matchup> findPendingMatchupRequestsByUserId(@Param("id") Long id);
}
