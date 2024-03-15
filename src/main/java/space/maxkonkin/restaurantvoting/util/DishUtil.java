package space.maxkonkin.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import space.maxkonkin.restaurantvoting.error.NotFoundException;
import space.maxkonkin.restaurantvoting.model.Dish;
import space.maxkonkin.restaurantvoting.to.DishTo;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DishUtil {
    public static Dish fromTo(DishTo to) {
        return new Dish(to.getId(), to.getDescription(), to.getPrice());
    }

    public static DishTo makeTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getDescription(), dish.getPrice());
    }

    public static List<Dish> fromTos(List<DishTo> tos) {
        return tos.stream().map(DishUtil::fromTo).collect(Collectors.toList());
    }

    public static List<DishTo> makeTos(List<Dish> dishes) {
        return dishes.stream().map(DishUtil::makeTo).collect(Collectors.toList());
    }

    public static void checkDishBelongsToMenu(Dish dish, int menuId) {
        if (dish.getMenu().getId() != menuId) {
            throw new NotFoundException(String.format(
                    "Dish with id=%d not found for menu with id=%d",
                    dish.getId(), menuId));
        }
    }
}
