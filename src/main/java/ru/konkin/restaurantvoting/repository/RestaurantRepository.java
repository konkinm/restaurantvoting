package ru.konkin.restaurantvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.konkin.restaurantvoting.error.NotFoundException;
import ru.konkin.restaurantvoting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.votes v WHERE v.voteDate = curdate()")
    List<Restaurant> getAllWithTodayVotes();

    @EntityGraph(attributePaths = {"menu"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithMenu(int id);

    default Restaurant getExistedWithMenu(int id) {
        return getWithMenu(id).orElseThrow(() ->
                new NotFoundException("Restaurant with id=" + id + " not found"));
    }

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menu d WHERE r.id=?1 AND d.restaurant.id = ?1 AND d.localDate = curdate()")
    Optional<Restaurant> getWithTodayMenu(int id);

    default Restaurant getExistedWithTodayMenu(int id) {
        return getWithTodayMenu(id).orElseThrow(() ->
                new NotFoundException("Restaurant with id=" + id + " not found"));
    }
}
