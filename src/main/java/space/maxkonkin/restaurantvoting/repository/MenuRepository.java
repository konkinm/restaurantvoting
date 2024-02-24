package space.maxkonkin.restaurantvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import space.maxkonkin.restaurantvoting.error.NotFoundException;
import space.maxkonkin.restaurantvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m FROM Menu m WHERE m.id = :id AND m.restaurant.id = :restaurantId")
    Optional<Menu> get(int id, int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = ?1 ORDER BY m.menuDate DESC")
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> getAll(int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.menuDate = :date")
    Optional<Menu> getByDate(int restaurantId, LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.id = :id AND m.restaurant.id = :restaurantId")
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Menu> getWithDishes(int id, int restaurantId);

    default Menu getExistedWithDishes(int id, int restaurantId) {
        return getWithDishes(id, restaurantId).orElseThrow(() ->
                new NotFoundException("Menu with id=" + id +
                        " for restaurant with id=" + restaurantId + " not found"));
    }

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.menuDate = :date")
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Menu> getByDateWithDishes(int restaurantId, LocalDate date);

    default Menu getExistedByDateWithDishes(int restaurantId, LocalDate date) {
        return getByDateWithDishes(restaurantId, date).orElseThrow(() ->
                new NotFoundException("Menu for date " + date +
                        " for restaurant with id=" + restaurantId + " not found"));
    }

    @Query("SELECT m FROM Menu m WHERE m.id = :id AND m.restaurant.id = :restaurantId")
    @EntityGraph(attributePaths = {"restaurant", "dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Menu> getWithDishesAndRestaurant(int id, int restaurantId);

    default Menu getExistedWithDishesAndRestaurant(int id, int restaurantId) {
        return getWithDishesAndRestaurant(id, restaurantId).orElseThrow(() ->
                new NotFoundException("Menu with id=" + id +
                        " for restaurant with id=" + restaurantId + " not found"));
    }
}
