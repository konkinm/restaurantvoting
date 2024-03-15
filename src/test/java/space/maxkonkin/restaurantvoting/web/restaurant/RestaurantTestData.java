package space.maxkonkin.restaurantvoting.web.restaurant;

import space.maxkonkin.restaurantvoting.model.Restaurant;
import space.maxkonkin.restaurantvoting.to.RestaurantTo;
import space.maxkonkin.restaurantvoting.web.MatcherFactory;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus", "votes");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class, "todayVotes", "todayMenu");

    public static final int KHEDI_ID = 1;
    public static final long KHEDI_TODAY_VOTES_COUNT = 1;
    public static final int KHACHAPURI_HOUSE_ID = 2;
    public static final int NOT_FOUND_ID = 100;

    public static final Restaurant khedi = new Restaurant(KHEDI_ID, "Khedi");
    public static final Restaurant khachapuriHouse = new Restaurant(KHACHAPURI_HOUSE_ID, "Khachapuri House");

    public static Restaurant getNew() {
     return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(KHEDI_ID, "Updated");
    }
}
