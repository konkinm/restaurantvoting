package ru.konkin.restaurantvoting.web.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.model.Dish;
import ru.konkin.restaurantvoting.repository.DishRepository;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.konkin.restaurantvoting.web.RestValidation.checkNotFoundWithId;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {
    static final String REST_URL = "api/restaurants";

    @Autowired
    protected DishRepository dishRepository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @GetMapping("/{restaurantId}/menu")
    @JsonView(View.BasicInfo.class)
    public List<Dish> getAllByDate(@PathVariable int restaurantId,
                                    @RequestParam(required = false) @DateTimeFormat(
                                            iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menu for restaurant with id={} for date {}", restaurantId, date);
        checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);
        return dishRepository.getByDate(restaurantId, date == null ? LocalDate.now() : date);
    }

    @GetMapping("/{restaurantId}/menu/{id}")
    @JsonView(View.BasicInfo.class)
    public Dish get(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("get {} restaurant with id={}", id, restaurantId);
        return dishRepository.getExistedRestaurantDish(id, restaurantId);
    }
}
