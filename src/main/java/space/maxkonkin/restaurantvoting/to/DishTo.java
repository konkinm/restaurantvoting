package space.maxkonkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import space.maxkonkin.restaurantvoting.validation.NoHtml;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "description", "price"})
public class DishTo extends BaseTo {
    @NotBlank@Size(max = 255)
    @NotBlank
    @Size(max = 255)
    @NoHtml
    @JsonProperty("description")
    private String description;

    @NotNull@Range(min = 0, max = 1000000)
    @NotNull
    @Range(min = 0, max = 1000000)
    @JsonProperty("price")
    private Integer price;

    public DishTo() {
    }

    public DishTo(Integer id, String description, Integer price) {
        super(id);
        this.description = description;
        this.price = price;
    }
}
