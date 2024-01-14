package ru.konkin.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.konkin.restaurantvoting.View;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    @JsonView(View.BasicInfo.class)
    long todayVotes;

    public RestaurantTo(int id, String name, long todayVotes) {
        super(id, name);
        this.todayVotes = todayVotes;
    }
}
