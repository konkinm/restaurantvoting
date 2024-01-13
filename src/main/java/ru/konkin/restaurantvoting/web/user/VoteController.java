package ru.konkin.restaurantvoting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.error.IllegalRequestDataException;
import ru.konkin.restaurantvoting.model.Restaurant;
import ru.konkin.restaurantvoting.model.User;
import ru.konkin.restaurantvoting.model.Vote;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;
import ru.konkin.restaurantvoting.repository.VoteRepository;
import ru.konkin.restaurantvoting.web.AuthUser;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/profile/votes";

    private static final LocalTime VOTE_TIME_LIMIT = LocalTime.of(11, 0);

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    @JsonView(View.BasicInfo.class)
    public Vote getTodaysVote(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get today's vote for {}", authUser);
        return voteRepository.getExistedLast(authUser.id());
    }

    @GetMapping("/history")
    @JsonView(View.BasicInfo.class)
    public List<Vote> getHistory(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get votes history for {}", authUser);
        return voteRepository.getHistory(authUser.id());
    }

    @PutMapping
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @JsonView(View.BasicInfo.class)
    public ResponseEntity<?> vote(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("{} votes for restaurant with id={}", authUser, restaurantId);
        if (LocalTime.now().isBefore(VOTE_TIME_LIMIT)) {
            User user = authUser.getUser();
            Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
            return voteRepository.getLast(authUser.id())
                    .map(vote -> {
                        if (vote.getRestaurant().getId() != restaurantId) {
                            vote.setRestaurant(restaurant);
                            voteRepository.save(vote);
                            return ResponseEntity.noContent().build();
                        }
                        return ResponseEntity.ok().build();
                    })
                    .orElseGet(() -> {
                        Vote created = new Vote(user, restaurant);
                        created = voteRepository.save(created);
                        return ResponseEntity.created(
                                        ServletUriComponentsBuilder.fromCurrentContextPath()
                                                .path(REST_URL).build().toUri())
                                .body(created);
                    });
        } else {
            throw new IllegalRequestDataException(String.format("You should vote before %s", VOTE_TIME_LIMIT));
        }
    }
}
