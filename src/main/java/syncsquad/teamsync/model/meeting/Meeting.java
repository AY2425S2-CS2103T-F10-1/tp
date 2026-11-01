package syncsquad.teamsync.model.meeting;

import static syncsquad.teamsync.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import syncsquad.teamsync.commons.util.ToStringBuilder;

/**
 * Represents a Meeting in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Meeting implements Comparable<Meeting> {
    public static final String MESSAGE_CONSTRAINTS =
            "Meeting time should be given in the following format: dd-mm-yyyy HH:mm HH:mm";

    public static final DateTimeFormatter DATE_TO_STRING_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter TIME_TO_STRING_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Title title;

    /**
     * Creates a Meeting with the specified date, start time, end time and title.
     * Every field must be present and not null.
     */
    public Meeting(LocalDate date, LocalTime startTime, LocalTime endTime, Title title) {
        requireAllNonNull(date, startTime, endTime, title);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public String getDay() {
        switch (date.getDayOfWeek()) {
        case MONDAY:
            return "MON";
        case TUESDAY:
            return "TUE";
        case WEDNESDAY:
            return "WED";
        case THURSDAY:
            return "THU";
        case FRIDAY:
            return "FRI";
        case SATURDAY:
            return "SAT";
        case SUNDAY:
            return "SUN";
        default:
            return "MON";
        }
    }

    /**
     * Returns a string representation of the date of the meeting.
     */
    public String getDateString() {
        return date.format(DATE_TO_STRING_FORMATTER);
    }

    /**
     * Returns a string representation of the start time of the meeting.
     */
    public String getStartTimeString() {
        return startTime.format(TIME_TO_STRING_FORMATTER);
    }

    /**
     * Returns a string representation of the end time of the meeting.
     */
    public String getEndTimeString() {
        return endTime.format(TIME_TO_STRING_FORMATTER);
    }

    /**
     * Returns the date of the meeting.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Returns the start time of the meeting.
     */
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the end time of the meeting.
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * Returns the title of the meeting.
     */
    public Title getTitle() {
        return this.title;
    }

    /**
     * Returns the date and time at which the meeting starts.
     */
    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, startTime);
    }

    /**
     * Returns true if both meetings overlap in terms of timing.
     * Both start and end time are considered to be non-inclusive,
     * i.e. endTime of 12pm and startTime of 12pm of another meeting is allowed.
     */
    public boolean isOverlapping(Meeting otherMeeting) {
        boolean timeOverlap = this.getEndTime().isAfter(otherMeeting.getStartTime())
                && this.getStartTime().isBefore(otherMeeting.getEndTime());
        boolean dateOverlap = this.getDate().equals(otherMeeting.getDate());
        return timeOverlap && dateOverlap;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Meeting)) {
            return false;
        }

        Meeting otherMeeting = (Meeting) other;
        return date.equals(otherMeeting.date) && startTime.equals(otherMeeting.startTime)
                && endTime.equals(otherMeeting.endTime) && title.equals(otherMeeting.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime, title);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("date", date)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("title", title)
                .toString();
    }

    @Override
    public int compareTo(Meeting other) {
        if (other == null) {
            throw new NullPointerException();
        }

        return getStartDateTime().compareTo(other.getStartDateTime());
    }
}
