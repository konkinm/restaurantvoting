package space.maxkonkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {
    @NotNull
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @JsonProperty(value = "restaurant_id", access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // https://stackoverflow.com/a/28025008/548473
    private final Integer restaurantId;

    @NotNull
    @NotNull
    @JsonProperty("dishes")
    private final List<DishTo> dishes;

    public MenuTo(Integer id, LocalDate date, Integer restaurantId, List<DishTo> dishes) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }
}
