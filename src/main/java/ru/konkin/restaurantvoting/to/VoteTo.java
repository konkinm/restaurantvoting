package ru.konkin.restaurantvoting.to;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    @NotNull
    Integer restaurantId;

    public VoteTo(Integer id, Integer restaurantId) {
        super(id);
        this.restaurantId = restaurantId;
    }
}
