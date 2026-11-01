package syncsquad.teamsync.testutil;

import static syncsquad.teamsync.logic.parser.ParserUtil.DATE_FORMATTER;
import static syncsquad.teamsync.logic.parser.ParserUtil.TIME_FORMATTER;

import java.time.LocalDate;
import java.time.LocalTime;

import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.meeting.Title;

/**
 * A utility class to help with building Meeting objects.
 */
public class MeetingBuilder {

    public static final String DEFAULT_DATE = "31-03-2025";
    public static final String DEFAULT_START_TIME = "14:00";
    public static final String DEFAULT_END_TIME = "16:00";

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Title title;

    /**
     * Creates a {@code MeetingBuilder} with the default details.
     */
    public MeetingBuilder() {
        date = LocalDate.parse(DEFAULT_DATE, DATE_FORMATTER);
        startTime = LocalTime.parse(DEFAULT_START_TIME, TIME_FORMATTER);
        endTime = LocalTime.parse(DEFAULT_END_TIME, TIME_FORMATTER);
        title = Title.DEFAULT;
    }

    /**
     * Initializes the MeetingBuilder with the data of {@code personToCopy}.
     */
    public MeetingBuilder(Meeting meetingToCopy) {
        date = meetingToCopy.getDate();
        startTime = meetingToCopy.getStartTime();
        endTime = meetingToCopy.getEndTime();
        title = meetingToCopy.getTitle();
    }


    /**
     * Sets the {@code date} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDate(String date) {
        this.date = LocalDate.parse(date, DATE_FORMATTER);
        return this;
    }

    /**
     * Sets the {@code startTime} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime, TIME_FORMATTER);
        return this;
    }

    /**
     * Sets the {@code endTime} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime, TIME_FORMATTER);
        return this;
    }

    /**
     * Sets the {@code title} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withTitle(String title) {
        this.title = new Title(title);
        return this;
    }

    public Meeting build() {
        return new Meeting(date, startTime, endTime, title);
    }

}
