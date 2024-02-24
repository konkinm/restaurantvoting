package space.maxkonkin.restaurantvoting.repository;

import org.springframework.transaction.annotation.Transactional;
import space.maxkonkin.restaurantvoting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
