package space.maxkonkin.restaurantvoting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import space.maxkonkin.restaurantvoting.HasId;

@Setter
@Getter
@EqualsAndHashCode
public abstract class BaseTo implements HasId {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // https://stackoverflow.com/a/28025008/548473
    protected Integer id;

    protected BaseTo(Integer id) {
        this.id = id;
    }

    public BaseTo() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseTo;
    }

}
