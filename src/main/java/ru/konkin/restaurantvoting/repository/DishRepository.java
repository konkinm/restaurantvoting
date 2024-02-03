package ru.konkin.restaurantvoting.repository;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.error.NotFoundException;
import ru.konkin.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.restaurant.id = :restaurantId")
    Optional<Dish> get(int id, int restaurantId);

    default Dish getExistedRestaurantDish(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(() ->
                new NotFoundException("Dish with id=" + id + " for restaurant with id=" + restaurantId + " not found"));
    }

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 AND d.localDate =?2 ORDER BY d.description ASC")
    List<Dish> getByDate(int restaurantId, LocalDate date);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Dish d WHERE d.id=?1")
    @JsonView(View.BasicInfo.class)
    Optional<Dish> getWithRestaurant(int id);

    default Dish getExistedWithRestaurant(int id) {
        return getWithRestaurant(id).orElseThrow(() ->
                new NotFoundException("Dish with id=" + id + " not found"));
    }
}