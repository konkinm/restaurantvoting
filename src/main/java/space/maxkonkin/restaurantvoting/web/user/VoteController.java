package space.maxkonkin.restaurantvoting.web.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import space.maxkonkin.restaurantvoting.error.IllegalRequestDataException;
import space.maxkonkin.restaurantvoting.model.Restaurant;
import space.maxkonkin.restaurantvoting.model.Vote;
import space.maxkonkin.restaurantvoting.repository.RestaurantRepository;
import space.maxkonkin.restaurantvoting.repository.VoteRepository;
import space.maxkonkin.restaurantvoting.to.VoteTo;
import space.maxkonkin.restaurantvoting.util.VoteUtil;
import space.maxkonkin.restaurantvoting.web.AuthUser;
import space.maxkonkin.restaurantvoting.web.RestValidation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @GetMapping("/today")
    public Optional<VoteTo> getToday(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get today's vote for {}", authUser);
        return voteRepository.getByDate(authUser.id(), LocalDate.now()).map(VoteUtil::getTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<VoteTo> createLast(@Valid @RequestBody VoteTo voteTo,
                                             @AuthenticationPrincipal AuthUser authUser) {
        log.info("{} creates vote for restaurant with id={}", authUser, voteTo.getRestaurantId());
        RestValidation.checkNew(voteTo);
        var user = authUser.getUser();
        var restaurant = restaurantRepository.getExisted(voteTo.getRestaurantId());
        Optional<Vote> vote = voteRepository.getByDate(authUser.id(), LocalDate.now());
        if (vote.isEmpty()) {
            var created = new Vote(user, restaurant);
            created = voteRepository.save(created);
            return ResponseEntity.created(
                            ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(REST_URL + "/last").build().toUri())
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
            Optional<Vote> vote = voteRepository.getByDate(authUser.id(), LocalDate.now());
            if (vote.isPresent() && !Objects.equals(vote.get().getRestaurant().getId(), restaurant.getId())) {
                vote.get().setRestaurant(restaurant);
                voteRepository.save(vote.get());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().build();
        } else {
            throw new IllegalRequestDataException(String.format("You can update today's vote only before %s",
                    VOTE_TIME_LIMIT));
        }
    }
}
