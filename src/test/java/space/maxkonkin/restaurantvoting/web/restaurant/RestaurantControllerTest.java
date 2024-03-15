package space.maxkonkin.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.maxkonkin.restaurantvoting.to.RestaurantTo;
import space.maxkonkin.restaurantvoting.util.RestaurantUtil;
import space.maxkonkin.restaurantvoting.web.AbstractControllerTest;
import space.maxkonkin.restaurantvoting.web.user.UserTestData;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.maxkonkin.restaurantvoting.web.restaurant.RestaurantTestData.RESTAURANT_TO_MATCHER;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = RestaurantController.REST_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllWithTodayVotesAndMenu() throws Exception {
        List<RestaurantTo> expected = RestaurantUtil.getTos(List.of(RestaurantTestData.khachapuriHouse, RestaurantTestData.khedi));
        expected.getLast().setId(RestaurantTestData.KHEDI_ID); // not working via `mvn test` without reassign
        perform(MockMvcRequestBuilders.get(RestaurantController.REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(expected));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getToday() throws Exception {
        RestaurantTo expected = RestaurantUtil.getTo(RestaurantTestData.khedi);
        expected.setId(RestaurantTestData.KHEDI_ID); // not working via `mvn test` without reassign
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.KHEDI_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(expected));
    }
}
