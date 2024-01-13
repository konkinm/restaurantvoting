package ru.konkin.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.model.Dish;
import ru.konkin.restaurantvoting.repository.DishRepository;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;

import java.util.List;

import static ru.konkin.restaurantvoting.web.RestValidation.checkNotFoundWithId;

@RestController
@RequestMapping(value = MenuController.REST_URL)
@Slf4j
public class MenuController {
    static final String REST_URL = "api/profile/restaurants";

    @Autowired
    protected DishRepository dishRepository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @GetMapping("/{restaurantId}/menu")
    @JsonView(View.BasicInfo.class)
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get today menu for restaurant with id={}", restaurantId);
        checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);
        return dishRepository.getTodayMenu(restaurantId);
    }

    @GetMapping("/{restaurantId}/menu/{id}")
    @JsonView(View.BasicInfo.class)
    public Dish get(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("get {} for {}", id, restaurantId);
        return dishRepository.getExistedRestaurantDish(id, restaurantId);
    }
}
