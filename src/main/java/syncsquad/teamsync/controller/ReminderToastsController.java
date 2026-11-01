package syncsquad.teamsync.controller;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import syncsquad.teamsync.viewmodel.ReminderViewModel;

/**
 * A stack of toasts, one for each meeting reminder in the {@code ReminderViewModel}.
 * A toast stays until the user closes it.
 */
public class ReminderToastsController extends UiPart<VBox> {
    private static final String FXML = "ReminderToasts.fxml";

    @FXML
    private VBox toastBox;

    /**
     * Creates a {@code ReminderToastsController} showing the reminders in {@code viewModel}.
     */
    public ReminderToastsController(ReminderViewModel viewModel) {
        super(FXML);
        viewModel.getReminders().addListener((ListChangeListener<String>) change -> {
            toastBox.getChildren().clear();
            change.getList().forEach(reminder -> toastBox.getChildren().add(createToast(viewModel, reminder)));
        });
    }

    private static Notification createToast(ReminderViewModel viewModel, String reminder) {
        Notification toast = new Notification(reminder, new FontIcon(Material2AL.ALARM));
        toast.getStyleClass().addAll(Styles.ACCENT, Styles.ELEVATED_2);
        toast.setPrefWidth(360);
        toast.setOnClose(event -> viewModel.dismiss(reminder));
        return toast;
    }
}
