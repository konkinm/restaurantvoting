package ru.konkin.restaurantvoting.web;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.model.Dish;
import ru.konkin.restaurantvoting.repository.DishRepository;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;
import ru.konkin.restaurantvoting.to.DishTo;

import java.net.URI;
import java.util.List;

import static ru.konkin.restaurantvoting.util.DishUtil.checkDishBelongsToRestaurant;
import static ru.konkin.restaurantvoting.util.DishUtil.fromTo;
import static ru.konkin.restaurantvoting.web.RestValidation.*;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {
    static final String REST_URL = "api/admin/restaurants";

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/{restaurantId}/menu")
    @JsonView(View.BasicInfo.class)
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get today menu for restaurant with id={}", restaurantId);
        assureEntityExists(restaurantRepository.existsById(restaurantId), restaurantId);
        return dishRepository.getTodayMenu(restaurantId);
    }

    @GetMapping("/{restaurantId}/menu/{id}")
    @JsonView(View.BasicInfo.class)
    public Dish get(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("get {} for {}", id, restaurantId);
        return dishRepository.getExistedRestaurantDish(id, restaurantId);
    }

    @PostMapping(path = "/{restaurantId}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.BasicInfo.class)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create {}", dishTo);
        checkNew(dishTo);
        Dish dish = fromTo(dishTo);
        dish.setRestaurant(restaurantRepository.getExisted(restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}/menu/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restaurantId}/menu/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonView(View.BasicInfo.class)
    public void update(@Valid @RequestBody DishTo dishTo,
                       @PathVariable("id") int id,
                       @PathVariable("restaurantId") int restaurantId) {
        log.info("update {} with id={} for restaurant with id={}", dishTo, id, restaurantId);
        assureIdConsistent(dishTo, id);
        Dish old = dishRepository.getExistedWithRestaurant(id);
        checkDishBelongsToRestaurant(old, restaurantId);
        Dish dish = fromTo(dishTo);
        dish.setId(old.getId());
        dish.setRestaurant(old.getRestaurant());
        dishRepository.save(dish);
    }

    @DeleteMapping("/{restaurantId}/menu/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("delete {} for restaurant with id={}", id, restaurantId);
        checkDishBelongsToRestaurant(dishRepository.getExistedWithRestaurant(id), restaurantId);
        dishRepository.delete(id);
    }
}
