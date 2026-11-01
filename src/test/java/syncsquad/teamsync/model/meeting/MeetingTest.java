package syncsquad.teamsync.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static syncsquad.teamsync.logic.commands.CommandTestUtil.VALID_DATE_SEP_MEETING;
import static syncsquad.teamsync.logic.commands.CommandTestUtil.VALID_END_TIME_SEP_MEETING;
import static syncsquad.teamsync.logic.commands.CommandTestUtil.VALID_START_TIME_SEP_MEETING;
import static syncsquad.teamsync.logic.parser.ParserUtil.DATE_FORMATTER;
import static syncsquad.teamsync.logic.parser.ParserUtil.TIME_FORMATTER;
import static syncsquad.teamsync.testutil.TypicalAddressBook.FEB_MEETING;
import static syncsquad.teamsync.testutil.TypicalAddressBook.JAN_MEETING;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.testutil.MeetingBuilder;

public class MeetingTest {

    @Test
    public void getDay() {
        Meeting sunMeeting = new MeetingBuilder().withDate("6-4-2025").build();
        assertEquals("SUN", sunMeeting.getDay());
        Meeting monMeeting = new MeetingBuilder().withDate("7-4-2025").build();
        assertEquals("MON", monMeeting.getDay());
        Meeting tueMeeting = new MeetingBuilder().withDate("8-4-2025").build();
        assertEquals("TUE", tueMeeting.getDay());
        Meeting wedMeeting = new MeetingBuilder().withDate("9-4-2025").build();
        assertEquals("WED", wedMeeting.getDay());
        Meeting thuMeeting = new MeetingBuilder().withDate("10-4-2025").build();
        assertEquals("THU", thuMeeting.getDay());
        Meeting friMeeting = new MeetingBuilder().withDate("11-4-2025").build();
        assertEquals("FRI", friMeeting.getDay());
        Meeting satMeeting = new MeetingBuilder().withDate("12-4-2025").build();
        assertEquals("SAT", satMeeting.getDay());
    }

    @Test
    public void hashCodeMethod() {
        LocalDate date = LocalDate.parse("5-4-2025", DATE_FORMATTER);
        LocalTime startTime = LocalTime.parse("14:00", TIME_FORMATTER);
        LocalTime endTime = LocalTime.parse("15:00", TIME_FORMATTER);
        Meeting meeting = new Meeting(date, startTime, endTime, Title.DEFAULT);
        assertEquals(Objects.hash(date, startTime, endTime, Title.DEFAULT), meeting.hashCode());
    }

    @Test
    public void equals() {
        // same values -> returns true
        Meeting janMeetingCopy = new MeetingBuilder(JAN_MEETING).build();
        assertTrue(JAN_MEETING.equals(janMeetingCopy));

        // same object -> returns true
        assertTrue(JAN_MEETING.equals(JAN_MEETING));

        // null -> returns false
        assertFalse(JAN_MEETING.equals(null));

        // different type -> returns false
        assertFalse(JAN_MEETING.equals(5));

        // different person -> returns false
        assertFalse(JAN_MEETING.equals(FEB_MEETING));

        // different date -> returns false
        Meeting editedJanMeeting = new MeetingBuilder(JAN_MEETING).withDate(VALID_DATE_SEP_MEETING).build();
        assertFalse(JAN_MEETING.equals(editedJanMeeting));

        // different start time -> returns false
        editedJanMeeting = new MeetingBuilder(JAN_MEETING).withStartTime(VALID_START_TIME_SEP_MEETING).build();
        assertFalse(JAN_MEETING.equals(editedJanMeeting));

        // different end time -> returns false
        editedJanMeeting = new MeetingBuilder(JAN_MEETING).withEndTime(VALID_END_TIME_SEP_MEETING).build();
        assertFalse(JAN_MEETING.equals(editedJanMeeting));

        // different title -> returns false
        editedJanMeeting = new MeetingBuilder(JAN_MEETING).withTitle("Retrospective").build();
        assertFalse(JAN_MEETING.equals(editedJanMeeting));
    }

    @Test
    public void toStringMethod() {
        String expected = Meeting.class.getCanonicalName() + "{date=" + JAN_MEETING.getDate()
                + ", startTime=" + JAN_MEETING.getStartTime() + ", endTime=" + JAN_MEETING.getEndTime()
                + ", title=" + JAN_MEETING.getTitle() + "}";
        assertEquals(expected, JAN_MEETING.toString());
    }
}
