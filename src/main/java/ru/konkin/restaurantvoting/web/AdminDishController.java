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
    private DishRepository repository;

    @GetMapping
    @JsonView(View.BasicInfo.class)
    public List<Dish> getAll() {
        log.info("get all");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(View.BasicInfo.class)
    public Dish get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    @GetMapping("/{id}/with-restaurant")
    @JsonView(View.DishInfo.class)
    public Dish getWithRestaurant(@PathVariable int id) {
        log.info("get {} with restaurant", id);
        return repository.getExistedWithRestaurant(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        repository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
