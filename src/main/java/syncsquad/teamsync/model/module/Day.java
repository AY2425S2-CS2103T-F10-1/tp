package syncsquad.teamsync.model.module;

import static java.util.Objects.requireNonNull;
import static syncsquad.teamsync.commons.util.AppUtil.checkArgument;

import java.util.List;

/**
 * Represents a Day of the week.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 */
public class Day {
    public static final String MESSAGE_CONSTRAINTS =
            "Day should be first 3 letters of the day of week (case insensitive) "
            + "(e.g MON/TUE/sat)";
    public static final List<String> VALID_DAYS = List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    public final String day;

    /**
     * Constructs a {@code Day}.
     *
     * @param day A valid day of week.
     */
    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        this.day = day.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid day.
     */
    public static boolean isValidDay(String test) {
        return VALID_DAYS.contains(test.toUpperCase());
    }

    @Override
    public String toString() {
        return day;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Day)) {
            return false;
        }

        Day otherDay = (Day) other;
        return day.equals(otherDay.day);
    }

    @Override
    public int hashCode() {
        return this.day.hashCode();
    }
}
