package ru.konkin.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.konkin.restaurantvoting.HasId;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.validation.NoHtml;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity implements HasId {
    @NotBlank
    @Size(min = 2, max = 128)
    @Column(name = "name", nullable = false, unique = true)
    @NoHtml
    @JsonView(View.BasicInfo.class)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("description ASC")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonManagedReference
    @JsonView(View.AdditionalInfo.class)
    private List<Dish> menu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @Column(name = "vote")
    @OrderBy("voteDate DESC")
    @JsonView(View.AdditionalInfo.class)
    @Schema(hidden = true)
    private Set<Vote> votes;

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
