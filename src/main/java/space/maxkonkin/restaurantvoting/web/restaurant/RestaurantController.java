package space.maxkonkin.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.maxkonkin.restaurantvoting.repository.MenuRepository;
import space.maxkonkin.restaurantvoting.repository.RestaurantRepository;
import space.maxkonkin.restaurantvoting.repository.VoteRepository;
import space.maxkonkin.restaurantvoting.to.MenuTo;
import space.maxkonkin.restaurantvoting.to.RestaurantTo;
import space.maxkonkin.restaurantvoting.util.MenuUtil;
import space.maxkonkin.restaurantvoting.util.RestaurantUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    @Autowired
    protected RestaurantRepository restaurantRepository;
    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected VoteRepository voteRepository;

    @GetMapping
    @Cacheable("restaurants")
    public List<RestaurantTo> getAllWithTodayVotesAndMenu() {
        log.info("get all with today's votes and menu");
        List<RestaurantTo> tos = RestaurantUtil.getTos(restaurantRepository.findAll());
        tos.forEach(to -> {
            to.setTodayMenu(MenuUtil.getToFromOptional((menuRepository
                    .getByDateWithDishes(to.getId(), LocalDate.now()))));
            to.setTodayVotes(voteRepository.getCountByDate(to.getId(), LocalDate.now()));
        });
        return tos;
    } // not solving N+1 problem TODO: replace with minimal amount of JPQL queries in RestaurantRepository

    @GetMapping("/{id}")
    @Cacheable("restaurants")
    public RestaurantTo getToday(@PathVariable int id) {
        log.info("get the restaurant with id={}", id);
        long todayVotes = voteRepository.getCountByDate(id, LocalDate.now());
        MenuTo menu = MenuUtil.getTo(menuRepository.getExistedByDateWithDishes(id, LocalDate.now()));
        return RestaurantUtil.getTo(restaurantRepository.getExisted(id), todayVotes, menu);
    }
}
