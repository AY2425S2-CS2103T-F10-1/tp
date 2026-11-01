package syncsquad.teamsync.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.testutil.MeetingBuilder;

public class ReminderServiceTest {
    // Starts at 14:00 on 01-04-2025
    private static final Meeting MEETING = new MeetingBuilder().withDate("01-04-2025")
            .withStartTime("14:00").withEndTime("15:00").withTitle("Standup").build();
    private static final List<Meeting> MEETINGS = List.of(MEETING);

    private final ReminderService service = new ReminderService();

    private static LocalDateTime at(String time) {
        return LocalDateTime.parse("2025-04-01T" + time);
    }

    @Test
    public void checkReminders_remindsOnceWithinLeadTime() {
        assertEquals(List.of(), service.checkReminders(MEETINGS, at("13:44:59"), 15));
        assertEquals(MEETINGS, service.checkReminders(MEETINGS, at("13:45:00"), 15));
        assertEquals(List.of(), service.checkReminders(MEETINGS, at("13:50:00"), 15));
    }

    @Test
    public void checkReminders_firstCheckAfterReminderTime_remindsUntilMeetingStarts() {
        // e.g. the meeting was added, or the app opened, 5 minutes before the meeting starts
        assertEquals(MEETINGS, service.checkReminders(MEETINGS, at("13:59:59"), 15));
        assertEquals(List.of(), new ReminderService().checkReminders(MEETINGS, at("14:00:00"), 15));
    }

    @Test
    public void checkReminders_remindersOff_remindsLaterWhenTurnedOn() {
        assertEquals(List.of(), service.checkReminders(MEETINGS, at("13:50:00"), 0));
        assertEquals(MEETINGS, service.checkReminders(MEETINGS, at("13:51:00"), 15));
    }

    @Test
    public void checkReminders_leadTimeIncreased_remindsWithNewLeadTime() {
        assertEquals(List.of(), service.checkReminders(MEETINGS, at("13:30:00"), 15));
        assertEquals(MEETINGS, service.checkReminders(MEETINGS, at("13:31:00"), 60));
    }

    @Test
    public void checkReminders_meetingDeletedAndAddedAgain_remindsAgain() {
        assertEquals(MEETINGS, service.checkReminders(MEETINGS, at("13:50:00"), 15));
        assertEquals(List.of(), service.checkReminders(List.of(), at("13:51:00"), 15));
        assertEquals(MEETINGS, service.checkReminders(MEETINGS, at("13:52:00"), 15));
    }

    @Test
    public void checkReminders_reminderTimeOnPreviousDay_reminds() {
        Meeting earlyMeeting = new MeetingBuilder().withDate("02-04-2025")
                .withStartTime("00:10").withEndTime("01:00").build();
        assertEquals(List.of(earlyMeeting),
                service.checkReminders(List.of(earlyMeeting, MEETING), at("23:55:00"), 15));
    }
}
