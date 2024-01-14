package ru.konkin.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.format.annotation.DateTimeFormat;
import ru.konkin.restaurantvoting.HasId;
import ru.konkin.restaurantvoting.View;
import ru.konkin.restaurantvoting.validation.NoHtml;

import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames =
        {"description", "restaurant_id", "local_date"}, name = "dish_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends BaseEntity implements HasId {

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(max = 255)
    @NoHtml
    @JsonView(View.BasicInfo.class)
    private String description;

    @Column(name = "price", nullable = false, columnDefinition = "int default 0")
    @Range(min = 0, max = 1000000)
    @JsonView(View.BasicInfo.class)
    private int price;

    @Column(name = "local_date", nullable = false, columnDefinition = "date default curdate()")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonView(View.AdditionalInfo.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate localDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonBackReference
    @JsonView(View.AdditionalInfo.class)
    @Schema(hidden = true)
    private Restaurant restaurant;

    public Dish(String description, int price, LocalDate localDate) {
        this.description = description;
        this.price = price;
        this.localDate = localDate;
    }

    public Dish(String description, int price) {
        this(description, price, LocalDate.now());
    }
}
