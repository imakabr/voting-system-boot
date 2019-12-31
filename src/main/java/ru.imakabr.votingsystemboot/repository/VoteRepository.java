package ru.imakabr.votingsystemboot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.imakabr.votingsystemboot.model.User;
import ru.imakabr.votingsystemboot.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=?1 and v.user.id=?2")
    int delete(int voteId, int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 ORDER BY v.date desc")
    List<Vote> getAllVotesByUserId(int id);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT  v FROM Vote v WHERE v.restaurant.id=?1 ORDER BY v.date desc")
    List<Vote> getAllVotesByRestaurantId(int id);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 and v.date=?2")
    Vote getVoteByUserIdAndDate(int id, LocalDate date);

    @Query("SELECT v.user FROM Vote v WHERE v.restaurant.id=?1 and v.date=?2")
    List<User> getAllUsersByRestaurantIdAndDate(int id, LocalDate date);
}
