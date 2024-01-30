package ru.konkin.restaurantvoting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.konkin.restaurantvoting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
