package space.maxkonkin.restaurantvoting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import space.maxkonkin.restaurantvoting.HasId;
import space.maxkonkin.restaurantvoting.validation.NoHtml;

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
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("menuDate DESC")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private List<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("voteDate DESC")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
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
