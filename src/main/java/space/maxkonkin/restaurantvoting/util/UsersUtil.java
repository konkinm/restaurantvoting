package space.maxkonkin.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import space.maxkonkin.restaurantvoting.model.Role;
import space.maxkonkin.restaurantvoting.model.User;
import space.maxkonkin.restaurantvoting.to.UserTo;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}