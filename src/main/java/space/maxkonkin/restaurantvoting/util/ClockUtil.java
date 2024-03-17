package space.maxkonkin.restaurantvoting.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.time.Clock;

@UtilityClass
public class ClockUtil {
    @Getter
    @Setter
    private Clock clock = Clock.systemDefaultZone();
}
