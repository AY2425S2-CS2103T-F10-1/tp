package syncsquad.teamsync.controller;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import syncsquad.teamsync.commons.core.LogsCenter;

/**
 * Controller for a help dialog
 */
public class HelpDialogController extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(HelpDialogController.class);
    private static final String FXML = "HelpDialog.fxml";

    @FXML
    private VBox mainVBox;

    @FXML
    private TableView<CommandSummary> commandTableView;

    @FXML
    private TableColumn<CommandSummary, String> categoryColumn;

    @FXML
    private TableColumn<CommandSummary, String> actionColumn;

    @FXML
    private TableColumn<CommandSummary, String> formatColumn;

    @FXML
    private TableColumn<CommandSummary, String> exampleColumn;

    /**
     * Constructs a {@code HelpDialogController}
     */
    public HelpDialogController() {
        super(FXML);
        mainVBox.setStyle("-fx-background-color: -color-bg-default");
    }

    /**
     * Initializes the HelpDialogController.
     */
    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));
        exampleColumn.setCellValueFactory(new PropertyValueFactory<>("example"));

        commandTableView.setItems(getCommandSummaryData());
    }

    /**
     * Opens the user guide in the default web browser. Logs a warning if the
     * operation fails.
     */
    @FXML
    private void openUserGuide() {
        try {
            Desktop.getDesktop().browse(new URI("https://ay2425s2-cs2103t-f10-1.github.io/tp/UserGuide.html"));
        } catch (Exception e) {
            logger.warning("Failed to open User Guide URL: " + e.getMessage());
        }
    }

    /**
     * Returns the data for the command summary table.
     */
    private ObservableList<CommandSummary> getCommandSummaryData() {
        return FXCollections.observableArrayList(
                new CommandSummary("Student", "Add a teammate",
                        "person add -n NAME -p PHONE_NUMBER -e EMAIL -a ADDRESS [-t TAG]… [-m MODULE]...",
                        "person add -n John Doe -p 98765432 -e johndoe@example.com -a John St. Blk 123, #01-01"),
                new CommandSummary("Student", "Edit a teammate",
                        "person edit INDEX [-n NAME] [-p PHONE] [-e EMAIL] [-a ADDRESS] [-t TAG]…",
                        "person edit 1 -p 91234567 -e johndoe@example.com"),
                new CommandSummary("Student", "Delete a teammate",
                        "person delete INDEX", "person delete 1"),
                new CommandSummary("Student", "List all teammates",
                        "person list", "person list"),
                new CommandSummary("Student", "Search for a teammate",
                        "person find KEYWORD [MORE_KEYWORDS]", "person find James Jake"),
                new CommandSummary("Module", "Add a module for a teammate",
                        "module add INDEX MODULE_CODE DAY START_TIME END_TIME", "module add 1 cs2101 thu 12:00 15:00"),
                new CommandSummary("Module", "Delete a module from a teammate",
                        "module delete INDEX MODULE_CODE", "module delete 1 cs2101"),
                new CommandSummary("Meeting", "Add a meeting",
                        "meeting add DATE START_TIME END_TIME", "meeting 27-03-2025 12:00 15:00"),
                new CommandSummary("Meeting", "Delete a meeting",
                        "meeting delete INDEX", "meeting delete 1"),
                new CommandSummary("General", "View help",
                        "help", "help"),
                new CommandSummary("General", "Change week displayed",
                        "showdate DATE", "showdate 04-04-2025"),
                new CommandSummary("General", "Clear all data", "clear", "clear"),
                new CommandSummary("General", "Exit TeamSync", "exit", "exit")
        );
    }

}
