package space.maxkonkin.restaurantvoting.repository;

import org.springframework.data.jpa.repository.Query;
import space.maxkonkin.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.voteDate = :date")
    Optional<Vote> getByDate(int userId, LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 ORDER BY v.voteDate DESC")
    List<Vote> getHistory(int userId);

    @Query("SELECT COUNT(*) FROM Vote v WHERE v.restaurant.id = :restaurantId AND v.voteDate = :date")
    long getCountByDate(int restaurantId, LocalDate date);
}
