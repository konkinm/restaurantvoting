package ru.konkin.restaurantvoting.repository;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.error.NotFoundException;
import ru.konkin.restaurantvoting.model.Dish;

import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Dish d WHERE d.id=?1")
    @JsonView(View.BasicInfo.class)
    Optional<Dish> getWithRestaurant(int id);

    default Dish getExistedWithRestaurant(int id) {
        return getWithRestaurant(id).orElseThrow(() ->
                new NotFoundException("Entity with id=" + id + " not found"));
    }
}