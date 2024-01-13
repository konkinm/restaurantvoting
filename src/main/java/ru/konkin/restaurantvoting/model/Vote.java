package ru.konkin.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.konkin.restaurantvoting.View;

import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames =
        {"user_id", "restaurant_id", "vote_date"}, name = "vote_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(hidden = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonView(View.BasicInfo.class)
    private Restaurant restaurant;

    @Column(name = "vote_date", columnDefinition = "date default curdate()")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonView(View.BasicInfo.class)
    private LocalDate voteDate;

    public Vote(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
        this.voteDate = LocalDate.now();
    }
}
