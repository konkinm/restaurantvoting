package ru.konkin.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "dish")
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

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonView(View.BasicInfo.class)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
    @JsonView(View.DishInfo.class)
    @NotNull
    private Restaurant restaurant;
}
