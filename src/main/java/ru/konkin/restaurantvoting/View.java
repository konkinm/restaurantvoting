package ru.konkin.restaurantvoting;

public class View {
    public static class IdOnly {}

    public static class BasicInfo extends IdOnly {}

    public static class RestaurantInfo extends BasicInfo{}

    public static class MenuInfo extends BasicInfo {}

    public static class RestaurantInfo extends BasicInfo {}
}
