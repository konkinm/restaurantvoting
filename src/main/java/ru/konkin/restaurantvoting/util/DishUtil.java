package ru.konkin.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.konkin.restaurantvoting.error.IllegalRequestDataException;
import ru.konkin.restaurantvoting.model.Dish;
import ru.konkin.restaurantvoting.to.DishTo;

@UtilityClass
public class DishUtil {
    public static Dish fromTo(DishTo to) {
        return new Dish(to.getDescription(), to.getPrice(), to.getDate());
    }

    public static void checkDishBelongsToRestaurant(Dish dish, int restaurantId) {
        if (dish.getRestaurant().getId() != restaurantId) {
            throw new IllegalRequestDataException(String.format(
                    "Dish with id=%d belongs to restaurant with id=%d, but request's path restaurant id=%d",
                    dish.getId(), dish.getRestaurant().getId(), restaurantId));
        }
    }
}
