package ru.konkin.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import ru.konkin.restaurantvoting.error.NotFoundException;
import ru.konkin.restaurantvoting.model.Vote;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.voteDate = curdate()")
    Optional<Vote> getLast(int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 ORDER BY v.voteDate DESC")
    List<Vote> getHistory(int userId);

    default Vote getExistedLast(int userId) {
        return getLast(userId).orElseThrow(() ->
                new NotFoundException("Today's vote not found for user with id=" + userId));
    }
}
