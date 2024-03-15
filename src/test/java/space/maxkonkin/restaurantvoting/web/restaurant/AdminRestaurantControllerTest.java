package space.maxkonkin.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.maxkonkin.restaurantvoting.model.Restaurant;
import space.maxkonkin.restaurantvoting.repository.RestaurantRepository;
import space.maxkonkin.restaurantvoting.util.RestaurantUtil;
import space.maxkonkin.restaurantvoting.web.AbstractControllerTest;
import space.maxkonkin.restaurantvoting.web.user.UserTestData;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.maxkonkin.restaurantvoting.util.JsonUtil.writeValue;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(RestaurantUtil.getTo(newRestaurant))))
                .andDo(print())
                .andExpect(status().isCreated());
        Restaurant created = RestaurantUtil.fromTo(RestaurantTestData.RESTAURANT_TO_MATCHER.readFromJson(action));
        int newId = created.getId();
        newRestaurant.setId(newId);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(created, restaurantRepository.getExisted(newId));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Restaurant duplicate = RestaurantTestData.khedi;
        duplicate.setId(null);
        perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(RestaurantUtil.getTo(duplicate))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Unique index or primary key violation")));
    } //TODO: make unique name validator

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RestaurantTestData.KHEDI_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(RestaurantUtil.getTo(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(RestaurantTestData.KHEDI_ID), RestaurantTestData.getUpdated());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.KHEDI_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RestaurantTestData.KHEDI_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
