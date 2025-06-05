package LevelUP.Exception;

public class UserAlreadyInRankedException extends RuntimeException {
    public UserAlreadyInRankedException(String message) {
        super(message);
    }
}
