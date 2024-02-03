package ru.konkin.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import ru.konkin.restaurantvoting.error.NotFoundException;
import ru.konkin.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.voteDate = ?2")
    Optional<Vote> getByDate(int userId, LocalDate localDate);

    default Vote getExistedByDate(int userId, LocalDate localDate) {
        return getByDate(userId, localDate).orElseThrow(() ->
                new NotFoundException("Today's vote not found for user with id=" + userId));
    }

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 ORDER BY v.voteDate DESC")
    List<Vote> getHistory(int userId);
}
