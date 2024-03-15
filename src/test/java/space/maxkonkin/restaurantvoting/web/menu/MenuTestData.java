package space.maxkonkin.restaurantvoting.web.menu;

import space.maxkonkin.restaurantvoting.model.Dish;
import space.maxkonkin.restaurantvoting.model.Menu;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishes");
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class, "dishes");

    public static final int KHEDI_MENU_ID = 1;
    public static final int KHEDI_TODAY_MENU_ID = 2;
    public static final int KhH_MENU_ID = 4;
    public static final int NOT_FOUND_MENU_ID = 100;
    public static final int KHACHAPURI_ID = 1;
    public static final int ADJAPSANDALI_ID = 2;
    public static final int PKHALI_ID = 4;
    public static final int TEA_ID = 5;
    public static final int COFFEE_ID = 6;
    public static final int MINERAL_WATER_ID = 8;

    public static final Dish khachapuri = new Dish(KHACHAPURI_ID, "Khachapuri", 500);
    public static final Dish adjapsandali = new Dish(ADJAPSANDALI_ID,"Adjapsandali", 1800);
    public static final Dish coffee = new Dish(COFFEE_ID,"Coffee", 700);
    public static final Dish tea = new Dish(TEA_ID, "Tea", 500);
    public static final Dish pkhali = new Dish(PKHALI_ID, "Pkhali", 1600);
    public static final Dish mineraWater =new Dish(MINERAL_WATER_ID, "Mineral water", 350);

    public static final Dish newDish = new Dish("new dish", 100);
    public static final Dish updatedDish = new Dish("updated dish", 150);
    public static final Menu khediMenu = new Menu(KHEDI_MENU_ID, LocalDate.of(2024, 1, 11), List.of(khachapuri, tea));
    public static final Menu khediTodayMenu = new Menu(KHEDI_TODAY_MENU_ID, LocalDate.now(), List.of(adjapsandali, coffee));
    public static final Menu khhTodayMenu = new Menu(KhH_MENU_ID, LocalDate.now(), List.of(mineraWater, pkhali));

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2024,1,1), List.of(newDish));
    }

    public static Menu getUpdated() {
        return new Menu(KhH_MENU_ID, LocalDate.now().plusDays(1), List.of(updatedDish));
    }
}
