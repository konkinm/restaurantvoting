package space.maxkonkin.restaurantvoting.web.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import space.maxkonkin.restaurantvoting.model.User;
import space.maxkonkin.restaurantvoting.to.UserTo;
import space.maxkonkin.restaurantvoting.util.UsersUtil;
import space.maxkonkin.restaurantvoting.web.AuthUser;
import space.maxkonkin.restaurantvoting.web.RestValidation;

import java.net.URI;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
// TODO: cache only most requested data!
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        RestValidation.checkNew(userTo);
        var created = repository.prepareAndSave(UsersUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        RestValidation.assureIdConsistent(userTo, authUser.id());
        var user = authUser.getUser();
        repository.prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }
}