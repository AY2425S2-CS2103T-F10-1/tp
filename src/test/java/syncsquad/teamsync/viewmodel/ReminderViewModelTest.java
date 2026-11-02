package syncsquad.teamsync.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * Unit tests for {@link ReminderViewModel}.
 */
public class ReminderViewModelTest {
    private static final LocalDate DATE = LocalDate.of(2026, 11, 2);
    private static final LocalDateTime START = LocalDateTime.of(DATE, LocalTime.of(14, 0));
    private static final Meeting MEETING = new Meeting(DATE, LocalTime.of(14, 0), LocalTime.of(15, 0));
    private static final Meeting LATER_MEETING = new Meeting(DATE, LocalTime.of(16, 0), LocalTime.of(17, 0));

    private ObservableList<Meeting> meetingList;
    private MutableClock clock;
    private ReminderViewModel viewModel;

    /**
     * A clock whose time can be changed by the test.
     */
    private static class MutableClock extends Clock {
        private Instant instant;

        MutableClock(LocalDateTime dateTime) {
            setTime(dateTime);
        }

        void setTime(LocalDateTime dateTime) {
            instant = dateTime.toInstant(ZoneOffset.UTC);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }

    @BeforeEach
    public void setUp() {
        meetingList = FXCollections.observableArrayList();
        clock = new MutableClock(START.minusHours(1));
        viewModel = new ReminderViewModel(meetingList, clock, ReminderViewModel.DEFAULT_LEAD_TIME);
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ReminderViewModel(null, clock, ReminderViewModel.DEFAULT_LEAD_TIME));
        assertThrows(NullPointerException.class, () ->
                new ReminderViewModel(meetingList, null, ReminderViewModel.DEFAULT_LEAD_TIME));
        assertThrows(NullPointerException.class, () ->
                new ReminderViewModel(meetingList, clock, null));
    }

    @Test
    public void defaultLeadTime_isFifteenMinutes() {
        assertEquals(Duration.ofMinutes(15), ReminderViewModel.DEFAULT_LEAD_TIME);
    }

    @Test
    public void checkReminders_beforeWindow_noReminder() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(16));
        viewModel.checkReminders();
        assertTrue(viewModel.getActiveReminders().isEmpty());
    }

    @Test
    public void checkReminders_exactlyLeadTimeBefore_reminds() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(15));
        viewModel.checkReminders();
        assertEquals(1, viewModel.getActiveReminders().size());
        assertEquals(MEETING, viewModel.getActiveReminders().get(0));
    }

    @Test
    public void checkReminders_oneMinuteBefore_reminds() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(1));
        viewModel.checkReminders();
        assertEquals(1, viewModel.getActiveReminders().size());
    }

    @Test
    public void checkReminders_atStartTime_noReminder() {
        meetingList.add(MEETING);
        clock.setTime(START);
        viewModel.checkReminders();
        assertTrue(viewModel.getActiveReminders().isEmpty());
    }

    @Test
    public void checkReminders_onlyMeetingsInWindow_reminded() {
        meetingList.add(MEETING);
        meetingList.add(LATER_MEETING);
        clock.setTime(START.minusMinutes(10));
        viewModel.checkReminders();
        assertEquals(1, viewModel.getActiveReminders().size());
        assertEquals(MEETING, viewModel.getActiveReminders().get(0));
    }

    @Test
    public void checkReminders_calledTwice_remindsOnce() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(10));
        viewModel.checkReminders();
        clock.setTime(START.minusMinutes(9));
        viewModel.checkReminders();
        assertEquals(1, viewModel.getActiveReminders().size());
    }

    @Test
    public void meetingAdded_insideWindow_remindsImmediately() {
        clock.setTime(START.minusMinutes(10));
        meetingList.add(MEETING);
        assertEquals(1, viewModel.getActiveReminders().size());
    }

    @Test
    public void meetingDeleted_whileReminding_removesReminder() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(10));
        viewModel.checkReminders();
        meetingList.remove(MEETING);
        assertTrue(viewModel.getActiveReminders().isEmpty());
    }

    @Test
    public void dismiss_removesReminder_notShownAgain() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(10));
        viewModel.checkReminders();
        viewModel.dismiss(MEETING);
        assertTrue(viewModel.getActiveReminders().isEmpty());

        clock.setTime(START.minusMinutes(5));
        viewModel.checkReminders();
        assertTrue(viewModel.getActiveReminders().isEmpty());
    }

    @Test
    public void checkReminders_meetingStarted_removesReminder() {
        meetingList.add(MEETING);
        clock.setTime(START.minusMinutes(10));
        viewModel.checkReminders();
        clock.setTime(START);
        viewModel.checkReminders();
        assertTrue(viewModel.getActiveReminders().isEmpty());
    }

    @Test
    public void getActiveReminders_isUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> viewModel.getActiveReminders().add(MEETING));
    }
}
