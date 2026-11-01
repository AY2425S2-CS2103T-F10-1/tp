package syncsquad.teamsync.logic;

import static syncsquad.teamsync.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import syncsquad.teamsync.model.meeting.Meeting;

/**
 * Decides which meetings are due for a reminder.
 *
 * A meeting is due at the first check whose time falls within {@code [start - lead time, start)}.
 * Checking against that window, rather than against the exact reminder time, means a meeting is still
 * reminded when it is added, or the app is opened, after its reminder time but before it starts.
 * Each meeting is reminded at most once.
 */
public class ReminderService {
    private final Set<Meeting> remindedMeetings = new HashSet<>();

    /**
     * Returns the meetings in {@code meetings} that are due for a reminder at {@code now} and have not been
     * reminded before. Returns no meetings when {@code leadMinutes} is 0, which means reminders are off.
     */
    public List<Meeting> checkReminders(List<Meeting> meetings, LocalDateTime now, int leadMinutes) {
        requireAllNonNull(meetings, now);
        // Forget deleted meetings, so a meeting that is deleted and added again is reminded again.
        remindedMeetings.retainAll(meetings);
        if (leadMinutes == 0) {
            return List.of();
        }

        List<Meeting> dueMeetings = meetings.stream()
                .filter(meeting -> !remindedMeetings.contains(meeting))
                .filter(meeting -> !now.isBefore(meeting.getStartDateTime().minusMinutes(leadMinutes))
                        && now.isBefore(meeting.getStartDateTime()))
                .collect(Collectors.toList());
        remindedMeetings.addAll(dueMeetings);
        return dueMeetings;
    }
}
