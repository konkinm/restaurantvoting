package space.maxkonkin.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import space.maxkonkin.restaurantvoting.validation.NoHtml;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NamedTo extends BaseTo {
    @NotBlank@Size(min = 2, max = 128)
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    protected String name;

    public NamedTo() {
    }

    public NamedTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
