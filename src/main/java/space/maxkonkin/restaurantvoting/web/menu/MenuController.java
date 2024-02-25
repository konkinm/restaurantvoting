package space.maxkonkin.restaurantvoting.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import space.maxkonkin.restaurantvoting.repository.MenuRepository;
import space.maxkonkin.restaurantvoting.repository.RestaurantRepository;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.util.MenuUtil;
import space.maxkonkin.restaurantvoting.web.RestValidation;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {
    static final String REST_URL = "/api/restaurants";

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @GetMapping("/{restaurantId}/menus")
    @Cacheable("menus")
    public List<MenuTo> getAll(@PathVariable int restaurantId) {
        log.info("get menus for restaurant with id={}", restaurantId);
        RestValidation.checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);
        return MenuUtil.getTos(menuRepository.getAll(restaurantId));
    }

    @GetMapping("/{restaurantId}/menus/by-date")
    @Cacheable("menus")
    public MenuTo getByDate(@PathVariable int restaurantId,
                            @RequestParam @DateTimeFormat(
                                    iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menu for restaurant with id={} for date {}", restaurantId, date);
        RestValidation.checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);
        return MenuUtil.getTo(menuRepository.getExistedByDateWithDishes(restaurantId, date));
    }

    @GetMapping("/{restaurantId}/menus/{id}")
    @Cacheable("menus")
    public MenuTo get(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("get {} restaurant with id={}", id, restaurantId);
        RestValidation.checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);
        return MenuUtil.getTo(menuRepository.getExistedWithDishes(id, restaurantId));
    }
}
