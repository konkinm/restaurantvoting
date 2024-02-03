package ru.konkin.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.konkin.restaurantvoting.model.Vote;
import ru.konkin.restaurantvoting.to.VoteTo;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class VoteUtil {
    public VoteTo getTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getVoteDate());
    }

    public List<VoteTo> getTos(List<Vote> votes) {
        return votes.stream().map(VoteUtil::getTo).collect(Collectors.toList());
    }
}
