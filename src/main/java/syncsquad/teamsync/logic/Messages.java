package syncsquad.teamsync.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import syncsquad.teamsync.logic.parser.Prefix;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_MEETING_DISPLAYED_INDEX = "The meeting index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_MODULE = "The person does not have the provided module assigned to them";
    public static final String MESSAGE_INVALID_START_END_TIME = "End time must be after start time";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Modules: ");
        person.getModules().forEach(builder::append);
        builder.append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code meeting} for display to the user
     */
    public static String format(Meeting meeting) {
        return new StringBuilder()
                .append(meeting.getTitle())
                .append("; Date: ")
                .append(meeting.getDateString())
                .append("; Start time: ")
                .append(meeting.getStartTimeString())
                .append("; End time: ")
                .append(meeting.getEndTimeString())
                .toString();
    }

}
