package syncsquad.teamsync.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.testutil.MeetingBuilder;

public class ReminderViewModelTest {
    private static final Meeting MEETING = new MeetingBuilder().withDate("01-04-2025")
            .withStartTime("14:00").withEndTime("15:30").withTitle("Sprint planning").build();

    @Test
    public void tick_meetingDue_showsReminderAndUpdatesNow() {
        LocalDateTime start = LocalDateTime.parse("2025-04-01T13:00:00");
        LocalDateTime tickTime = LocalDateTime.parse("2025-04-01T13:45:10");
        ReminderViewModel viewModel = new ReminderViewModel(now -> now.equals(tickTime)
                ? List.of(MEETING) : List.of(), start);

        viewModel.tick(tickTime);

        assertEquals(tickTime, viewModel.nowProperty().get());
        // 14 min 50 s left rounds up to 15 min
        assertEquals(List.of("Sprint planning starts in 15 min (14:00 to 15:30)"), viewModel.getReminders());

        viewModel.dismiss(viewModel.getReminders().get(0));
        assertEquals(List.of(), viewModel.getReminders());
    }
}
