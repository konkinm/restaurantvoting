package space.maxkonkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MenuTo extends BaseTo {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // https://stackoverflow.com/a/28025008/548473
    int restaurantId;

    @NotNull
    List<DishTo> dishes;

    public MenuTo(Integer id, LocalDate date, int restaurantId, List<DishTo> dishes) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.dishes = dishes;
    }
}
