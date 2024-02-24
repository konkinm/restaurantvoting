package space.maxkonkin.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import space.maxkonkin.restaurantvoting.error.NotFoundException;
import space.maxkonkin.restaurantvoting.model.Menu;
import space.maxkonkin.restaurantvoting.to.MenuTo;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MenuUtil {
    public static Menu fromTo(MenuTo to) {
        return new Menu(to.getId(), to.getDate());
    }

    public static MenuTo getTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getMenuDate(), menu.getRestaurant().getId(),
                DishUtil.makeTos(menu.getDishes()));
    }

    public static MenuTo getTo(Menu menu, int restaurantId) {
        return new MenuTo(menu.getId(), menu.getMenuDate(), restaurantId,
                DishUtil.makeTos(menu.getDishes()));
    }

    public static List<MenuTo> getTos(List<Menu> menus) {
        return menus.stream().map(MenuUtil::getTo).collect(Collectors.toList());
    }

    public static void checkMenuBelongsToRestaurant(Menu menu, int restaurantId) {
        if (menu.getRestaurant().getId() != restaurantId) {
            throw new NotFoundException(String.format(
                    "Menu with id=%d not found for restaurant with id=%d",
                    menu.getId(), restaurantId));
        }
    }


}
