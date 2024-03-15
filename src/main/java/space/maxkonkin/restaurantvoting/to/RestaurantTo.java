package space.maxkonkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantTo extends NamedTo {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // https://stackoverflow.com/a/28025008/548473
    Long todayVotes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    // TODO: FIX Example Body field visibility https://stackoverflow.com/questions/53454357/
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    MenuTo todayMenu;

    public RestaurantTo(Integer id, String name, Long todayVotes, MenuTo todayMenu) {
        super(id, name);
        this.todayVotes = todayVotes;
        this.todayMenu = todayMenu;
    }
}
