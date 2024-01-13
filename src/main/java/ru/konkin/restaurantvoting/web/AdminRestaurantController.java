package ru.konkin.restaurantvoting.web;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.model.Restaurant;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.konkin.restaurantvoting.web.RestValidation.assureIdConsistent;
import static ru.konkin.restaurantvoting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController {
    static final String REST_URL = "api/admin/restaurants";

    @Autowired
    private RestaurantRepository repository;

    @GetMapping
    @JsonView(View.BasicInfo.class)
    public List<Restaurant> getAll() {
        log.info("get all");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/{id}")
    @JsonView(View.BasicInfo.class)
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    @GetMapping("/{id}/with-menu")
    @JsonView(View.MenuInfo.class)
    public Restaurant getWithMenu(@PathVariable int id) {
        log.info("get {} with menu", id);
        return repository.getExistedWithMenu(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.BasicInfo.class)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonView(View.BasicInfo.class)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
