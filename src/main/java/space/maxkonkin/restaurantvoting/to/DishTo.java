package space.maxkonkin.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import space.maxkonkin.restaurantvoting.validation.NoHtml;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DishTo extends BaseTo {
    @NotBlank
    @Size(max = 255)
    @NoHtml
    String description;

    @NotNull
    @Range(min = 0, max = 1000000)
    Integer price;

    public DishTo(Integer id, String description, Integer price) {
        super(id);
        this.description = description;
        this.price = price;
    }
}
