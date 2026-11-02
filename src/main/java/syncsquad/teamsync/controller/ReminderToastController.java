package syncsquad.teamsync.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.viewmodel.ReminderViewModel;

/**
 * Overlay that shows a toast for each upcoming meeting in {@link ReminderViewModel}.
 * It also runs the timer that periodically asks the view model to check for reminders.
 */
public class ReminderToastController extends UiPart<VBox> {
    private static final String FXML = "ReminderToast.fxml";
    private static final javafx.util.Duration CHECK_INTERVAL = javafx.util.Duration.seconds(30);
    private static final javafx.util.Duration ANIMATION_DURATION = javafx.util.Duration.millis(250);
    private static final double TOAST_WIDTH = 320;

    private final ReminderViewModel viewModel;
    private final Timeline timeline;
    private final Map<Meeting, Notification> toasts = new HashMap<>();

    @FXML
    private VBox toastContainer;

    /**
     * Creates a {@code ReminderToastController} bound to the given {@code ReminderViewModel}.
     */
    public ReminderToastController(ReminderViewModel viewModel) {
        super(FXML);
        this.viewModel = viewModel;

        viewModel.getActiveReminders().addListener((ListChangeListener<Meeting>) change -> {
            while (change.next()) {
                change.getRemoved().forEach(this::removeToast);
                change.getAddedSubList().forEach(this::addToast);
            }
        });
        viewModel.getActiveReminders().forEach(this::addToast);

        timeline = new Timeline(new KeyFrame(CHECK_INTERVAL, event -> viewModel.checkReminders()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Checks for reminders immediately, then every {@code CHECK_INTERVAL}.
     */
    public void start() {
        viewModel.checkReminders();
        timeline.play();
    }

    /**
     * Stops checking for reminders.
     */
    public void stop() {
        timeline.stop();
    }

    private void addToast(Meeting meeting) {
        Notification toast = new Notification(getMessage(meeting), new FontIcon(Material2AL.ALARM));
        toast.getStyleClass().addAll(Styles.ACCENT, Styles.ELEVATED_1, "reminder-toast");
        toast.setPrefWidth(TOAST_WIDTH);
        toast.setMaxWidth(TOAST_WIDTH);
        toast.setOnClose(event -> viewModel.dismiss(meeting));

        toasts.put(meeting, toast);
        toastContainer.getChildren().add(toast);
        Animations.slideInDown(toast, ANIMATION_DURATION).playFromStart();
    }

    private void removeToast(Meeting meeting) {
        Notification toast = toasts.remove(meeting);
        if (toast != null) {
            toastContainer.getChildren().remove(toast);
        }
    }

    private String getMessage(Meeting meeting) {
        LocalDateTime start = LocalDateTime.of(meeting.getDate(), meeting.getStartTime());
        long minutesLeft = Math.max(1, (long) Math.ceil(
                Duration.between(viewModel.now(), start).getSeconds() / 60.0));
        return String.format("Meeting on %s %s-%s starts in %d min",
                meeting.getDateString(), meeting.getStartTimeString(), meeting.getEndTimeString(), minutesLeft);
    }
}
