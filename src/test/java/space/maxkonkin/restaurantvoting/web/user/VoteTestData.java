package space.maxkonkin.restaurantvoting.web.user;

import space.maxkonkin.restaurantvoting.model.Vote;
import space.maxkonkin.restaurantvoting.to.VoteTo;
import space.maxkonkin.restaurantvoting.web.MatcherFactory;
import space.maxkonkin.restaurantvoting.web.restaurant.RestaurantTestData;

import java.time.LocalDate;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class);
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static final int TODAY_ADMIN_VOTE_ID = 1;
    private static final int USER_VOTE_ID = 2;
    public static final int ADMIN_VOTE_ID = 3;

    public static final VoteTo todayAdminVote = new VoteTo(TODAY_ADMIN_VOTE_ID, 1, LocalDate.now());
    public static final VoteTo adminVote = new VoteTo(ADMIN_VOTE_ID, 2, LocalDate.of(2024, 1, 12));
    public static final VoteTo userVote = new VoteTo(USER_VOTE_ID, 1, LocalDate.of(2024, 3, 10));

    public static Vote getNew() {
        return new Vote(UserTestData.user, RestaurantTestData.khedi);
    }

    public static Vote getUpdated() {
        return new Vote(UserTestData.admin, RestaurantTestData.khachapuriHouse);
    }
}
