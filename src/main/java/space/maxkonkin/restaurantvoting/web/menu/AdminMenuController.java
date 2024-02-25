package space.maxkonkin.restaurantvoting.web.menu;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import space.maxkonkin.restaurantvoting.error.IllegalRequestDataException;
import space.maxkonkin.restaurantvoting.repository.DishRepository;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.util.DishUtil;
import space.maxkonkin.restaurantvoting.util.MenuUtil;
import space.maxkonkin.restaurantvoting.web.RestValidation;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController extends MenuController {
    static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    private DishRepository dishRepository;

    @PostMapping(path = "/{restaurantId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public ResponseEntity<MenuTo> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create {}", menuTo);
        RestValidation.checkNew(menuTo);
        var menu = MenuUtil.fromTo(menuTo);
        menu.setRestaurant(restaurantRepository.getExisted(restaurantId));
        var created = menuRepository.save(menu);
        var dishes = DishUtil.fromTos(menuTo.getDishes());
        dishes.forEach(d -> d.setMenu(created));
        dishes = dishRepository.saveAll(dishes);
        menu.setDishes(dishes);
        var to = MenuUtil.getTo(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}/menus/{id}")
                .buildAndExpand(to.getRestaurantId(), to.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(to);
    }

    @PutMapping(value = "/{restaurantId}/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void update(@Valid @RequestBody MenuTo menuTo,
                       @PathVariable("id") int id,
                       @PathVariable("restaurantId") int restaurantId) {
        log.info("update {} with id={} for restaurant with id={}", menuTo, id, restaurantId);
        RestValidation.assureIdConsistent(menuTo, id);
        var existed = menuRepository.getExistedWithDishesAndRestaurant(id, restaurantId);
        MenuUtil.checkMenuBelongsToRestaurant(existed, restaurantId);
        final LocalDate date = menuTo.getDate();
        if (menuRepository.getByDate(restaurantId, date).isEmpty()) {
            dishRepository.deleteAll(existed.getDishes()); // deletes old entries -- ugly decision.
            // It would be better TODO: to make dedicated dish controllers
            existed.setMenuDate(date);
            var dishes = DishUtil.fromTos(menuTo.getDishes());
            dishes.forEach(d -> d.setMenu(existed));
            existed.setDishes(dishes);
        } else {
            throw new IllegalRequestDataException("Menu with date " + date + " already exists for the restaurant with id=" + restaurantId);
        }
    }

    @DeleteMapping("/{restaurantId}/menus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "menus", allEntries = true)
    public void delete(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("delete menu with id={} for restaurant with id={}", id, restaurantId);
        MenuUtil.checkMenuBelongsToRestaurant(menuRepository.getExistedWithDishesAndRestaurant(id, restaurantId), restaurantId);
        menuRepository.deleteExisted(id);
    }
}
