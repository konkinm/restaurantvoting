package ru.konkin.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.konkin.restaurantvoting.HasId;
import ru.konkin.restaurantvoting.View;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasId {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("price DESC")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonView(View.DishInfo.class)
    private List<Dish> dishes;
}
