package space.maxkonkin.restaurantvoting.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.maxkonkin.restaurantvoting.repository.MenuRepository;
import space.maxkonkin.restaurantvoting.web.AbstractControllerTest;
import space.maxkonkin.restaurantvoting.web.restaurant.RestaurantTestData;
import space.maxkonkin.restaurantvoting.web.user.UserTestData;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void createWithLocation() {
    }

    @Test
    void update() {
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.FIRST_RESTAURANT_ID + "/menus/" + MenuTestData.MENU_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.get(MenuTestData.MENU_ID, RestaurantTestData.FIRST_RESTAURANT_ID).isPresent());
    }
}
