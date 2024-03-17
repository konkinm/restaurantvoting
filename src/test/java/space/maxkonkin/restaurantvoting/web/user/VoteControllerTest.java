package space.maxkonkin.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import space.maxkonkin.restaurantvoting.model.Vote;
import space.maxkonkin.restaurantvoting.repository.VoteRepository;
import space.maxkonkin.restaurantvoting.to.VoteTo;
import space.maxkonkin.restaurantvoting.util.ClockUtil;
import space.maxkonkin.restaurantvoting.util.VoteUtil;
import space.maxkonkin.restaurantvoting.web.AbstractControllerTest;

import java.time.*;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.maxkonkin.restaurantvoting.util.JsonUtil.writeValue;

class VoteControllerTest extends AbstractControllerTest {
    public static final String REST_URL_SLASH = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        List<VoteTo> expected = List.of(VoteTestData.todayAdminVote, VoteTestData.adminVote);
        perform(MockMvcRequestBuilders.get(VoteController.REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_TO_MATCHER.contentJson(expected));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getToday() throws Exception {
        VoteTo expected = VoteTestData.todayAdminVote;
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_TO_MATCHER.contentJson(expected));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createLast() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(VoteUtil.getTo(newVote))))
                .andDo(print())
                .andExpect(status().isCreated());
        final VoteTo voteTo = VoteTestData.VOTE_TO_MATCHER.readFromJson(action);
        newVote.setId(voteTo.getId());
        VoteTestData.VOTE_TO_MATCHER.assertMatch(VoteUtil.getTo(repository.getByDate(UserTestData.ADMIN_ID, LocalDate.now()).orElseThrow()), VoteUtil.getTo(newVote));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateLastWithinTimeLimit() throws Exception {
        ClockUtil.setClock(Clock.fixed(LocalDateTime.of(LocalDate.now(), LocalTime.of(10,0))
                .toInstant(OffsetDateTime.now().getOffset()), Clock.systemDefaultZone().getZone()));
        Vote updated = VoteTestData.getUpdated();
        updated.setId(VoteTestData.TODAY_ADMIN_VOTE_ID);
        updated.setVoteDate(LocalDate.now());
        perform(MockMvcRequestBuilders.put(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(VoteUtil.getTo(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());
        VoteTestData.VOTE_TO_MATCHER.assertMatch(VoteUtil.getTo(repository.getByDate(UserTestData.ADMIN_ID, LocalDate.now()).orElseThrow()), VoteUtil.getTo(updated));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateLastOutOfTimeLimit() throws Exception {
        ClockUtil.setClock(Clock.fixed(LocalDateTime.of(LocalDate.now(), LocalTime.of(11,1)).toInstant(OffsetDateTime.now().getOffset()), Clock.systemDefaultZone().getZone()));
        Vote updated = VoteTestData.getUpdated();
        updated.setId(VoteTestData.TODAY_ADMIN_VOTE_ID);
        updated.setVoteDate(LocalDate.now());
        perform(MockMvcRequestBuilders.put(VoteController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(VoteUtil.getTo(updated))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}