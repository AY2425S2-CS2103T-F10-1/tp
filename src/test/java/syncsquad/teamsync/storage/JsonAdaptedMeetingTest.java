package syncsquad.teamsync.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static syncsquad.teamsync.storage.JsonAdaptedMeeting.DATE_FIELD;
import static syncsquad.teamsync.storage.JsonAdaptedMeeting.END_TIME_FIELD;
import static syncsquad.teamsync.storage.JsonAdaptedMeeting.MISSING_FIELD_MESSAGE_FORMAT;
import static syncsquad.teamsync.storage.JsonAdaptedMeeting.START_TIME_FIELD;
import static syncsquad.teamsync.testutil.Assert.assertThrows;
import static syncsquad.teamsync.testutil.TypicalAddressBook.FEB_MEETING;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.commons.exceptions.IllegalValueException;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.meeting.Title;

public class JsonAdaptedMeetingTest {
    private static final String INVALID_DATE = "later";
    private static final String INVALID_START_TIME = "now";
    private static final String INVALID_END_TIME = "3pm";


    private static final String VALID_DATE = FEB_MEETING.getDateString();
    private static final String VALID_START_TIME = FEB_MEETING.getStartTimeString();
    private static final String VALID_END_TIME = FEB_MEETING.getEndTimeString();
    private static final String VALID_TITLE = FEB_MEETING.getTitle().value;

    @Test
    public void toModelType_validMeetingDetails_returnsMeeting() throws Exception {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(FEB_MEETING);
        assertEquals(FEB_MEETING, meeting.toModelType());
    }

    @Test
    public void toModelType_nullMeeting_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new JsonAdaptedMeeting(null));
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(
                INVALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_TITLE);
        String expectedMessage = Meeting.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, meeting::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(null, VALID_START_TIME, VALID_END_TIME, VALID_TITLE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, DATE_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, meeting::toModelType);
    }

    @Test
    public void toModelType_invalidStartTime_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(
                VALID_DATE, INVALID_START_TIME, VALID_END_TIME, VALID_TITLE);
        String expectedMessage = Meeting.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, meeting::toModelType);
    }

    @Test
    public void toModelType_nullStartTime_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DATE, null, VALID_END_TIME, VALID_TITLE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, START_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, meeting::toModelType);
    }

    @Test
    public void toModelType_invalidEndTime_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(
                VALID_DATE, VALID_START_TIME, INVALID_END_TIME, VALID_TITLE);
        String expectedMessage = Meeting.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, meeting::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DATE, VALID_START_TIME, null, VALID_TITLE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, END_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, meeting::toModelType);
    }

    @Test
    public void toModelType_nullTitle_returnsMeetingWithDefaultTitle() throws Exception {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DATE, VALID_START_TIME, VALID_END_TIME, null);
        assertEquals(Title.DEFAULT, meeting.toModelType().getTitle());
    }

    @Test
    public void toModelType_invalidTitle_throwsIllegalValueException() {
        JsonAdaptedMeeting meeting = new JsonAdaptedMeeting(VALID_DATE, VALID_START_TIME, VALID_END_TIME, " ");
        assertThrows(IllegalValueException.class, Title.MESSAGE_CONSTRAINTS, meeting::toModelType);
    }

    @Test
    public void toModelType_endTimeBeforeStartTime_throwsIllegalValueException() {
        JsonAdaptedMeeting adaptedMeeting = new JsonAdaptedMeeting(
                VALID_DATE, VALID_END_TIME, VALID_START_TIME, VALID_TITLE);
        String expectedMessage = Meeting.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adaptedMeeting::toModelType);
    }
}
