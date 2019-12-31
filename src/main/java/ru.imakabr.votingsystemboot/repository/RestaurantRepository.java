package ru.imakabr.votingsystemboot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.imakabr.votingsystemboot.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @EntityGraph(attributePaths = {"items"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1 order by date_time desc")
    Restaurant getWithItems(int id);

    @EntityGraph(attributePaths = {"items"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1 AND date_time=?2")
    Restaurant getWithItemsByDate(int id, LocalDate date);

    @EntityGraph(attributePaths = {"items",}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Restaurant r JOIN r.items i on i.date=:date")
    List<Restaurant> getAllWithItemsByDate(@Param("date") LocalDate date);

}
