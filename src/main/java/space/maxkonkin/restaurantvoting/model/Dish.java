package space.maxkonkin.restaurantvoting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import space.maxkonkin.restaurantvoting.HasId;
import space.maxkonkin.restaurantvoting.validation.NoHtml;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames =
        {"menu_id", "price", "description"}, name = "dish_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends BaseEntity implements HasId {
    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 255)
    @NoHtml
    private String description;

    @Column(name = "price", nullable = false)
    @Range(min = 0, max = 1000000)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @Schema(hidden = true)
    private Menu menu;

    public Dish(String description, int price) {
        this.description = description;
        this.price = price;
    }
}
