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

import static ru.konkin.restaurantvoting.web.RestValidation.assureIdConsistent;
import static ru.konkin.restaurantvoting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    static final String REST_URL = "api/admin/dish";

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    @JsonView(View.BasicInfo.class)
    public List<Dish> getAll() {
        log.info("get all");
        return dishRepository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(View.BasicInfo.class)
    public Dish get(@PathVariable int id) {
        log.info("get {}", id);
        return dishRepository.getExisted(id);
    }

    @GetMapping("/{id}/with-restaurant")
    @JsonView(View.RestaurantInfo.class)
    public Dish getWithRestaurant(@PathVariable int id) {
        log.info("get {} with restaurant", id);
        return dishRepository.getExistedWithRestaurant(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.BasicInfo.class)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo) {
        log.info("create {}", dishTo);
        checkNew(dishTo);
        Dish fromTo = new Dish(dishTo.getDescription(), dishTo.getPrice());
        fromTo.setRestaurant(restaurantRepository.getExisted(dishTo.getRestaurantId()));
        Dish created = dishRepository.save(fromTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonView(View.BasicInfo.class)
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int id) {
        log.info("update {} with id={}", dishTo, id);
        assureIdConsistent(dishTo, id);
        Dish fromTo = new Dish(dishTo.getDescription(), dishTo.getPrice());
        fromTo.setId(id);
        fromTo.setRestaurant(restaurantRepository.getExisted(dishTo.getRestaurantId()));
        dishRepository.save(fromTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        dishRepository.deleteExisted(id);
    }
}
