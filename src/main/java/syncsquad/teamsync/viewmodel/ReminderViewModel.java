package syncsquad.teamsync.viewmodel;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * ViewModel for the clock-driven parts of the UI: the current time and the reminders on screen.
 * The UI calls {@link #tick(LocalDateTime)} periodically, which is the only way time advances here.
 */
public class ReminderViewModel {
    private final Function<LocalDateTime, List<Meeting>> checkReminders;
    private final ObjectProperty<LocalDateTime> now;
    private final ObservableList<String> reminders = FXCollections.observableArrayList();

    /**
     * Creates a {@code ReminderViewModel}.
     *
     * @param checkReminders returns the meetings due for a reminder at the given time
     * @param now the current time
     */
    public ReminderViewModel(Function<LocalDateTime, List<Meeting>> checkReminders, LocalDateTime now) {
        requireNonNull(checkReminders);
        requireNonNull(now);
        this.checkReminders = checkReminders;
        this.now = new SimpleObjectProperty<>(now);
    }

    public ObjectProperty<LocalDateTime> nowProperty() {
        return now;
    }

    /**
     * Returns the reminder messages currently shown, oldest first.
     */
    public ObservableList<String> getReminders() {
        return reminders;
    }

    /**
     * Advances the current time to {@code newNow} and shows a reminder for each meeting that is now due.
     */
    public void tick(LocalDateTime newNow) {
        requireNonNull(newNow);
        now.set(newNow);
        checkReminders.apply(newNow).forEach(meeting -> reminders.add(formatReminder(meeting, newNow)));
    }

    /**
     * Removes {@code reminder} from the reminders shown.
     */
    public void dismiss(String reminder) {
        reminders.remove(reminder);
    }

    /**
     * Returns the reminder message for {@code meeting} at {@code now}, which must be before the meeting starts.
     */
    static String formatReminder(Meeting meeting, LocalDateTime now) {
        long secondsLeft = Duration.between(now, meeting.getStartDateTime()).getSeconds();
        long minutesLeft = (secondsLeft + 59) / 60; // round up, so 14 min 10 s reads "15 min"
        return String.format("%s starts in %d min (%s to %s)", meeting.getTitle(), minutesLeft,
                meeting.getStartTimeString(), meeting.getEndTimeString());
    }
}
