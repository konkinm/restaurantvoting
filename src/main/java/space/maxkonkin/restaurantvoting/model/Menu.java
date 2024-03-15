package space.maxkonkin.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import space.maxkonkin.restaurantvoting.HasId;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames =
        {"restaurant_id", "menu_date"}, name = "menu_unique_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Menu extends BaseEntity implements HasId {
    @Column(name = "menu_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL) // TODO: assure that cascade is needed here
    @OrderBy("description ASC")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private List<Dish> dishes;

    public Menu(Integer id, LocalDate menuDate) {
        super(id);
        this.menuDate = menuDate;
    }

    public Menu(Integer id, LocalDate menuDate, List<Dish> dishes) {
        super(id);
        this.menuDate = menuDate;
        this.dishes = dishes;
    }
}
