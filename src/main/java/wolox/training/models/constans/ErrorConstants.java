package wolox.training.models.constans;

/**
 *  Class that Contains the messages of the pre-validators errors
 */
public class ErrorConstants {

    private ErrorConstants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * Preconditions message
     */
    public static final String NOT_NULL = "Please check the %s supplied, its null!.";
    public static final String NOT_EMPTY = "Please check the %s supplied, its empty!.";
    public static final String NOT_NUMERIC = "The %s isn't numeric.";
    public static final String NOT_GREATER_THAN = "The %s cannot smaller that %s.";
    public static final String NOT_LATER_CURRENT_DATE = "The %s cannot be later than the current date.";

}
