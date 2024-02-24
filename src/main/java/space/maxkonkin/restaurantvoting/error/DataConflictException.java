package space.maxkonkin.restaurantvoting.error;

public class DataConflictException extends AppException {
    public DataConflictException(String msg) {
        super(msg);
    }
}