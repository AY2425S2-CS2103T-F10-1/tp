package syncsquad.teamsync.storage;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import syncsquad.teamsync.commons.exceptions.IllegalValueException;
import syncsquad.teamsync.logic.parser.ParserUtil;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * Jackson-friendly version of {@link Meeting}.
 */
class JsonAdaptedMeeting {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Meeting's %s field is missing!";

    /** Field names used in missing field error message */
    public static final String DATE_FIELD = "Date";
    public static final String START_TIME_FIELD = "StartTime";
    public static final String END_TIME_FIELD = "EndTime";

    private final String date;
    private final String startTime;
    private final String endTime;

    /**
     * Constructs a {@code JsonAdaptedMeeting} with the given meeting details.
     */
    @JsonCreator
    public JsonAdaptedMeeting(@JsonProperty("date") String date, @JsonProperty("start time") String startTime,
                             @JsonProperty("end time") String endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Converts a given {@code Meeting} into this class for Jackson use.
     */
    public JsonAdaptedMeeting(Meeting source) {
        assert source != null;
        date = source.getDateString();
        startTime = source.getStartTimeString();
        endTime = source.getEndTimeString();
    }

    /**
     * Converts this Jackson-friendly adapted meeting object into the model's {@code Meeting} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Meeting toModelType() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, DATE_FIELD));
        }
        if (startTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, START_TIME_FIELD));
        }
        if (endTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, END_TIME_FIELD));
        }

        try {
            final LocalDate date = ParserUtil.parseDate(this.date);
            final LocalTime startTime = ParserUtil.parseTime(this.startTime);
            final LocalTime endTime = ParserUtil.parseTime(this.endTime);
            if (!endTime.isAfter(startTime)) {
                throw new IllegalValueException(Meeting.MESSAGE_CONSTRAINTS);
            }
            return new Meeting(date, startTime, endTime);
        } catch (ParseException e) {
            throw new IllegalValueException(Meeting.MESSAGE_CONSTRAINTS);
        }
    }

}
