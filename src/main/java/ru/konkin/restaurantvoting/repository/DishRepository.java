package ru.konkin.restaurantvoting.repository;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.error.NotFoundException;
import ru.konkin.restaurantvoting.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 AND d.localDate = curdate() ORDER BY d.description ASC")
    List<Dish> getTodayMenu(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.restaurant.id = :restaurantId")
    Optional<Dish> getRestaurantDish(int id, int restaurantId);

    default Dish getExistedRestaurantDish(int id, int restaurantId) {
        return getRestaurantDish(id, restaurantId).orElseThrow(() ->
                new NotFoundException("Dish with id=" + id + " for restaurantId=" + restaurantId + " not found"));
    }

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Dish d WHERE d.id=?1")
    @JsonView(View.BasicInfo.class)
    Optional<Dish> getWithRestaurant(int id);

    default Dish getExistedWithRestaurant(int id) {
        return getWithRestaurant(id).orElseThrow(() ->
                new NotFoundException("Dish with id=" + id + " not found"));
    }
}