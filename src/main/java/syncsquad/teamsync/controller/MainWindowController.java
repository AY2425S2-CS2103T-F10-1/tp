package syncsquad.teamsync.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import atlantafx.base.controls.ModalPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import syncsquad.teamsync.ResizeHelper;
import syncsquad.teamsync.commons.core.GuiSettings;
import syncsquad.teamsync.viewmodel.MainViewModel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindowController extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private Stage primaryStage;

    // Tracked here instead of Stage#maximizedProperty, which is unreliable for undecorated stages
    private final BooleanProperty isMaximized = new SimpleBooleanProperty(false);
    private Rectangle2D restoredBounds;

    // Independent Ui parts residing in this Ui container
    private MainViewModel viewModel;

    @FXML
    private StackPane mainStackPane;

    @FXML
    private SplitPane verticalSplitPane;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane meetingListPanelPlaceholder;

    @FXML
    private StackPane timetablePlaceholder;

    @FXML
    private SplitPane horizontalSplitPane;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane titleBarPlaceholder;

    @FXML
    private Label currentWeekLabel;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage}.
     */
    public MainWindowController(Stage primaryStage, MainViewModel viewModel) {
        super(FXML, primaryStage);

        this.viewModel = viewModel;

        // Set dependencies
        this.primaryStage = primaryStage;

        // Configure the UI
        setWindow(viewModel.getGuiSettings().get());

        // TODO: If we are adding `nextWeek` or `previousWeek` actions
        //  we should create a currentWeekController to handle the logic
        this.viewModel.getCurrentWeek()
            .addListener((unused1, oldValue, newValue) -> updateCurrentWeekLabel(newValue));
        updateCurrentWeekLabel(this.viewModel.getCurrentWeek().get());

    }

    /**
     * Returns the primary stage of the application.
     *
     * @return The primary stage.
     */
    protected Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Returns whether the window is maximized.
     */
    protected ReadOnlyBooleanProperty isMaximizedProperty() {
        return isMaximized;
    }

    /**
     * Maximizes the window to fill its screen, or restores it to its size and position before maximizing.
     * {@code Stage#setMaximized} is not used because it does not work for undecorated stages on macOS
     * (JDK-8255835) and reports a wrong maximized state there.
     */
    protected void setMaximized(boolean maximized) {
        if (maximized == isMaximized.get()) {
            return;
        }
        Rectangle2D bounds;
        if (maximized) {
            restoredBounds = new Rectangle2D(primaryStage.getX(), primaryStage.getY(),
                    primaryStage.getWidth(), primaryStage.getHeight());
            bounds = Screen.getScreensForRectangle(restoredBounds).stream().findFirst()
                    .orElse(Screen.getPrimary()).getVisualBounds();
        } else {
            bounds = restoredBounds;
        }
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        isMaximized.set(maximized);
    }

    /**
     * Returns the main view model associated with this controller.
     *
     * @return The main view model.
     */
    protected MainViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Returns the vertical divider position of the split pane.
     *
     * @return The vertical divider position.
     */
    protected double getVerticalDividerPosition() {
        return verticalSplitPane.getDividerPositions()[0];
    }

    /**
     * Returns the horizontal divider position of the split pane.
     *
     * @return The horizontal divider position.
     */
    protected double getHorizontalDividerPosition() {
        return horizontalSplitPane.getDividerPositions()[0];
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        // Added before the help modal pane so that the help dialog covers the toasts
        ReminderToastController reminderToast = new ReminderToastController(
            this.viewModel.getReminderViewModel());
        mainStackPane.getChildren().add(reminderToast.getRoot());
        StackPane.setAlignment(reminderToast.getRoot(), javafx.geometry.Pos.TOP_RIGHT);
        primaryStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> reminderToast.stop());
        reminderToast.start();

        ModalPane helpModalPane = new ModalPane();
        mainStackPane.getChildren().add(helpModalPane);
        StackPane.setAlignment(helpModalPane, javafx.geometry.Pos.CENTER);

        TitleBarController titleBarController = new TitleBarController(this, helpModalPane);
        titleBarPlaceholder.getChildren().add(titleBarController.getRoot());

        verticalSplitPane.setDividerPositions(viewModel.getGuiSettings().get().getVerticalDividerPosition());
        horizontalSplitPane.setDividerPositions(viewModel.getGuiSettings().get().getHorizontalDividerPosition());

        PersonTreeViewController personListPanel = new PersonTreeViewController(
            this.viewModel.getPersonListViewModel());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        MeetingTreeViewController meetingListPanel = new MeetingTreeViewController(
            this.viewModel.getMeetingListViewModel());
        meetingListPanelPlaceholder.getChildren().add(meetingListPanel.getRoot());

        TimetableController timetable = new TimetableController(
                this.viewModel.getPersonListViewModel(), this.viewModel.getMeetingListViewModel());
        timetablePlaceholder.getChildren().add(timetable.getRoot());

        ResultDisplayController resultDisplay = new ResultDisplayController(
            this.viewModel.getResultDisplayViewModel());
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooterController statusBarFooter = new StatusBarFooterController(
            this.viewModel.getAddressBookFilePath().get());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBoxController commandBox = new CommandBoxController(
            this.viewModel.getCommandBoxViewModel());
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        ResizeHelper.addResizeListener(primaryStage, isMaximized::get);
    }

    /**
     * Updates the current week label with the current week's date range.
     */
    private void updateCurrentWeekLabel(LocalDate startOfWeek) {
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        String dateFormat = "MMM d yyyy";
        String weekRange = String.format("%s to %s",
            startOfWeek.format(DateTimeFormatter.ofPattern(dateFormat)),
            endOfWeek.format(DateTimeFormatter.ofPattern(dateFormat)));
        currentWeekLabel.setText("Current Week: " + weekRange);
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindow(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
        setMaximized(guiSettings.getIsMaximized());
        verticalSplitPane.setDividerPositions(guiSettings.getVerticalDividerPosition());
        horizontalSplitPane.setDividerPositions(guiSettings.getHorizontalDividerPosition());
    }


    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        boolean wasMaximized = isMaximized.get();
        setMaximized(false);
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY(),
                verticalSplitPane.getDividerPositions()[0], horizontalSplitPane.getDividerPositions()[0],
                wasMaximized);
        viewModel.saveGuiSettings(guiSettings);
        primaryStage.hide();
    }
}
