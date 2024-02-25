package space.maxkonkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    @NotNull
    Integer restaurantId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate voteDate;

    public VoteTo(Integer id, Integer restaurantId, LocalDate voteDate) {
        super(id);
        this.restaurantId = restaurantId;
        this.voteDate = voteDate;
    }
}
