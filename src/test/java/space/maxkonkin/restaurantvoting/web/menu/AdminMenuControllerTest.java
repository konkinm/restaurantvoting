package space.maxkonkin.restaurantvoting.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.maxkonkin.restaurantvoting.model.Menu;
import space.maxkonkin.restaurantvoting.repository.MenuRepository;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.util.DishUtil;
import space.maxkonkin.restaurantvoting.util.MenuUtil;
import space.maxkonkin.restaurantvoting.web.AbstractControllerTest;
import space.maxkonkin.restaurantvoting.web.restaurant.RestaurantTestData;
import space.maxkonkin.restaurantvoting.web.user.UserTestData;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.maxkonkin.restaurantvoting.util.JsonUtil.writeValue;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + RestaurantTestData.KHEDI_ID + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MenuUtil.getTo(newMenu, RestaurantTestData.KHEDI_ID))))
                .andDo(print())
                .andExpect(status().isCreated());
        final MenuTo menuTo = MenuTestData.MENU_TO_MATCHER.readFromJson(action);
        Menu created = MenuUtil.fromTo(menuTo);
        created.setDishes(DishUtil.fromTos(menuTo.getDishes()));
        int newId = created.getId();
        newMenu.setId(newId);
        newMenu.getDishes().getFirst().setId(created.getDishes().getFirst().getId());
        MenuTestData.MENU_MATCHER.assertMatch(created, newMenu);
        MenuTestData.MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
        assertIterableEquals(newMenu.getDishes(), created.getDishes());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        ResultActions action = perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RestaurantTestData.KHACHAPURI_HOUSE_ID + "/menus/" + MenuTestData.KhH_MENU_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(MenuUtil.getTo(updated, RestaurantTestData.KHACHAPURI_HOUSE_ID))))
                .andDo(print())
                .andExpect(status().isNoContent());
        Menu menu = menuRepository.getExistedWithDishes(MenuTestData.KhH_MENU_ID, RestaurantTestData.KHACHAPURI_HOUSE_ID);
        MenuTestData.MENU_MATCHER.assertMatch(menu, updated);
        updated.getDishes().getFirst().setId(menu.getDishes().getFirst().getId());
        assertIterableEquals(updated.getDishes(), menu.getDishes());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.KHEDI_ID + "/menus/" + MenuTestData.KHEDI_TODAY_MENU_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.get(MenuTestData.KHEDI_TODAY_MENU_ID, RestaurantTestData.KHEDI_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.KHEDI_ID + "/menus/" + MenuTestData.NOT_FOUND_MENU_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
