package space.maxkonkin.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames =
        {"user_id", "vote_date", "restaurant_id"}, name = "vote_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "vote_date", columnDefinition = "date default curdate()")
    private LocalDate voteDate;

    public Vote(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
        this.voteDate = LocalDate.now();
    }
}
