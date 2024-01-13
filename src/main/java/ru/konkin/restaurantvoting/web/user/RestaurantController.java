package ru.konkin.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.model.Restaurant;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;
import ru.konkin.restaurantvoting.to.RestaurantTo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/profile/restaurants";

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @GetMapping
    public List<RestaurantTo> getAllWithVotesCounter() {
        log.info("get all with votes counter");
        return restaurantRepository.getAllWithTodayVotes().stream()
                .map(r -> new RestaurantTo(r.getId(), r.getName(), r.getVotes().size()))
                .sorted(Comparator.comparing(RestaurantTo::getTodayVotesCounter).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @JsonView(View.MenuInfo.class)
    public Restaurant getWithTodayMenu(@PathVariable int id) {
        log.info("get with today's menu for the restaurant with id={}", id);
        return restaurantRepository.getExistedWithTodayMenu(id);
    }
}
