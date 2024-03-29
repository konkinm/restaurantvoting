package space.maxkonkin.restaurantvoting.web;

import lombok.experimental.UtilityClass;
import space.maxkonkin.restaurantvoting.HasId;
import space.maxkonkin.restaurantvoting.error.IllegalRequestDataException;
import space.maxkonkin.restaurantvoting.error.NotFoundException;

@UtilityClass
public class RestValidation {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        if (!found) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }
}