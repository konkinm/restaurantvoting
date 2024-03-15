package space.maxkonkin.restaurantvoting.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.util.JsonUtil;
import space.maxkonkin.restaurantvoting.util.MenuUtil;
import space.maxkonkin.restaurantvoting.web.AbstractControllerTest;
import space.maxkonkin.restaurantvoting.web.restaurant.RestaurantTestData;
import space.maxkonkin.restaurantvoting.web.user.UserTestData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.maxkonkin.restaurantvoting.web.menu.MenuTestData.MENU_TO_MATCHER;

class MenuControllerTest extends AbstractControllerTest {
    public static final String REST_URL_SLASH = MenuController.REST_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAll() throws Exception {
        List<MenuTo> expected = MenuUtil.getTos(List.of(MenuTestData.khediTodayMenu, MenuTestData.khediMenu), RestaurantTestData.KHEDI_ID);
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.KHEDI_ID + "/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<MenuTo> menuTos = MENU_TO_MATCHER.readListFromJson(action);
        MENU_TO_MATCHER.assertMatch(menuTos, expected);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        MenuTo expected = MenuUtil.getTo(MenuTestData.khediMenu, RestaurantTestData.KHEDI_ID);
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.KHEDI_ID + "/menus/" + MenuTestData.KHEDI_MENU_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                //.andExpect(MENU_TO_MATCHER.contentJson(expected));
        final MenuTo menuTo = MENU_TO_MATCHER.readFromJson(action);
        //System.out.println(JsonUtil.writeValue(menuTo));
        MENU_TO_MATCHER.assertMatch(menuTo, expected);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByDate() throws Exception {
        MenuTo expected = MenuUtil.getTo(MenuTestData.khediTodayMenu, RestaurantTestData.KHEDI_ID);
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.KHEDI_ID + "/menus/by-date")
                .param("date", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHER.contentJson(expected));
    }
}