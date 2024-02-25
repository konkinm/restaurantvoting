package space.maxkonkin.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import space.maxkonkin.restaurantvoting.model.Restaurant;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantUtil {
    public static Restaurant fromTo(RestaurantTo to) {
        return new Restaurant(to.getId(), to.getName());
    }

    public static RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), null, null);
    }

    public static RestaurantTo getTo(Restaurant restaurant, Long voteCount, MenuTo menu) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), voteCount, menu);
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::getTo)
                .collect(Collectors.toList());
    }
}
