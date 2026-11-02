package syncsquad.teamsync.viewmodel;

import static syncsquad.teamsync.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * ViewModel that decides which meetings the user should be reminded of.
 * A meeting is due for a reminder when the current time falls within
 * {@code [start - leadTime, start)}. Each meeting is reminded at most once.
 */
public class ReminderViewModel {
    public static final Duration DEFAULT_LEAD_TIME = Duration.ofMinutes(15);

    private final ObservableList<Meeting> meetingList;
    private final Clock clock;
    private final Duration leadTime;

    private final ObservableList<Meeting> activeReminders = FXCollections.observableArrayList();
    private final ObservableList<Meeting> unmodifiableActiveReminders =
            FXCollections.unmodifiableObservableList(activeReminders);
    private final Set<Meeting> reminded = new HashSet<>();

    /**
     * Constructs a ReminderViewModel watching the given list of meetings.
     *
     * @param meetingList the observable list of meetings to watch
     * @param clock the clock used to tell the current time
     * @param leadTime how long before a meeting starts the reminder is shown
     */
    public ReminderViewModel(ObservableList<Meeting> meetingList, Clock clock, Duration leadTime) {
        requireAllNonNull(meetingList, clock, leadTime);
        this.meetingList = meetingList;
        this.clock = clock;
        this.leadTime = leadTime;

        // Re-check when meetings are added or deleted, instead of waiting for the next timer tick
        meetingList.addListener((ListChangeListener<Meeting>) change -> checkReminders());
    }

    /**
     * Returns the meetings that currently have a reminder showing.
     */
    public ObservableList<Meeting> getActiveReminders() {
        return unmodifiableActiveReminders;
    }

    public Duration getLeadTime() {
        return leadTime;
    }

    /**
     * Returns the current time according to this view model's clock.
     */
    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    /**
     * Adds reminders for meetings that have entered the lead time window,
     * and removes reminders for meetings that have started or no longer exist.
     */
    public void checkReminders() {
        LocalDateTime now = now();

        activeReminders.removeIf(meeting -> !meetingList.contains(meeting) || hasStarted(meeting, now));
        reminded.removeIf(meeting -> hasStarted(meeting, now));

        for (Meeting meeting : meetingList) {
            if (isDue(meeting, now) && reminded.add(meeting)) {
                activeReminders.add(meeting);
            }
        }
    }

    /**
     * Removes the reminder for {@code meeting}. It will not be shown again.
     */
    public void dismiss(Meeting meeting) {
        activeReminders.remove(meeting);
    }

    private boolean isDue(Meeting meeting, LocalDateTime now) {
        LocalDateTime start = getStart(meeting);
        return !now.isBefore(start.minus(leadTime)) && now.isBefore(start);
    }

    private static boolean hasStarted(Meeting meeting, LocalDateTime now) {
        return !now.isBefore(getStart(meeting));
    }

    private static LocalDateTime getStart(Meeting meeting) {
        return LocalDateTime.of(meeting.getDate(), meeting.getStartTime());
    }
}
