package syncsquad.teamsync.model.meeting;

import static java.util.Objects.requireNonNull;
import static syncsquad.teamsync.commons.util.AppUtil.checkArgument;

/**
 * Represents a Meeting's title in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTitle(String)}
 */
public class Title {

    public static final int MAX_LENGTH = 50;

    public static final String MESSAGE_CONSTRAINTS = "Meeting titles should not be blank, should not start or end "
            + "with whitespace, and should be at most " + MAX_LENGTH + " characters long";

    /** Title given to meetings that are added without one. */
    public static final Title DEFAULT = new Title("Meeting");

    public final String value;

    /**
     * Constructs a {@code Title}.
     *
     * @param title A valid title.
     */
    public Title(String title) {
        requireNonNull(title);
        checkArgument(isValidTitle(title), MESSAGE_CONSTRAINTS);
        value = title;
    }

    /**
     * Returns true if a given string is a valid title.
     */
    public static boolean isValidTitle(String test) {
        return !test.isBlank() && test.strip().equals(test) && test.length() <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Title)) {
            return false;
        }

        Title otherTitle = (Title) other;
        return value.equals(otherTitle.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
