package ru.konkin.restaurantvoting.web.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.konkin.restaurantvoting.error.IllegalRequestDataException;
import ru.konkin.restaurantvoting.model.Restaurant;
import ru.konkin.restaurantvoting.model.User;
import ru.konkin.restaurantvoting.model.Vote;
import ru.konkin.restaurantvoting.repository.RestaurantRepository;
import ru.konkin.restaurantvoting.repository.VoteRepository;
import ru.konkin.restaurantvoting.to.VoteTo;
import ru.konkin.restaurantvoting.util.VoteUtil;
import ru.konkin.restaurantvoting.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.konkin.restaurantvoting.web.RestValidation.checkNew;

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
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get votes history for {}", authUser);
        return VoteUtil.getTos(voteRepository.getHistory(authUser.id()));
    }

    @GetMapping("/last")
    public Optional<VoteTo> getLast(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get today's vote for {}", authUser);
        return voteRepository.getByDate(authUser.id(), LocalDate.now()).map(VoteUtil::getTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> createLast(@Valid @RequestBody VoteTo voteTo,
                                        @AuthenticationPrincipal AuthUser authUser) {
        log.info("{} creates vote for restaurant with id={}", authUser, voteTo.getRestaurantId());
        checkNew(voteTo);
        User user = authUser.getUser();
        Restaurant restaurant = restaurantRepository.getExisted(voteTo.getRestaurantId());
        Optional<Vote> vote = voteRepository.getByDate(authUser.id(), LocalDate.now());
        if (vote.isEmpty()) {
            Vote created = new Vote(user, restaurant);
            created = voteRepository.save(created);
            return ResponseEntity.created(
                            ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(REST_URL).build().toUri())
                    .body(VoteUtil.getTo(created));
        } else {
            throw new IllegalRequestDataException("Today's vote already exists");
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> updateLast(@Valid @RequestBody VoteTo voteTo,
                                        @AuthenticationPrincipal AuthUser authUser) {
        log.info("{} updates last vote for restaurant with id={}", authUser, voteTo.getRestaurantId());
        if (LocalTime.now().isBefore(VOTE_TIME_LIMIT)) {
            Restaurant restaurant = restaurantRepository.getExisted(voteTo.getRestaurantId());
            Vote vote = voteRepository.getExistedByDate(authUser.id(), LocalDate.now());
            if (!Objects.equals(vote.getRestaurant().getId(), restaurant.getId())) {
                vote.setRestaurant(restaurant);
                voteRepository.save(vote);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().build();
        } else {
            throw new IllegalRequestDataException(String.format("You can update today's vote only before %s",
                    VOTE_TIME_LIMIT));
        }
    }
}
