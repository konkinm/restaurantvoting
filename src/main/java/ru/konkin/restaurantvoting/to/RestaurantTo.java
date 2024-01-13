package ru.konkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.konkin.restaurantvoting.View;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    @JsonView(View.BasicInfo.class)
    long todayVotesCounter;

    public RestaurantTo(int id, String name, long todayVotesCounter) {
        super(id, name);
        this.todayVotesCounter = todayVotesCounter;
    }
}
