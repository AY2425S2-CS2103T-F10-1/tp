package syncsquad.teamsync.controller;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;

import atlantafx.base.controls.ModalPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane anchorPane;

    @FXML
    private VBox mainVBox;

    private ModalPane modalPane;

    @FXML
    private TableView<CommandSummary> commandTableView;

    @FXML
    private TableColumn<CommandSummary, String> actionColumn;

    @FXML
    private TableColumn<CommandSummary, String> formatColumn;

    @FXML
    private TableColumn<CommandSummary, String> exampleColumn;

    /**
     * Constructs a {@code HelpDialogController}
     */
    public HelpDialogController(ModalPane modalPane) {
        super(FXML);
        mainVBox.setStyle("-fx-background-color: -color-bg-default");
        this.modalPane = modalPane;
    }

    /**
     * Initializes the HelpDialogController.
     */
    @FXML
    public void initialize() {
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
     * Closes the help dialog.
     */
    @FXML
    private void closeHelpDialog() {
        modalPane.hide();
    }

    /**
     * Returns the data for the command summary table.
     */
    private ObservableList<CommandSummary> getCommandSummaryData() {
        return FXCollections.observableArrayList(
                new CommandSummary("Add a teammate",
                        "person add -n NAME -p PHONE_NUMBER -e EMAIL -a ADDRESS [-t TAG]… [-m MODULE]...",
                        "person add -n John Doe -p 12345678 -e johndoe@u.nus.edu -a RC4 "
                                + "-t Backend -m CS2101 Thu 12:00 15:00 -m CS2103T Fri 16:00 18:00"),
                new CommandSummary("Edit a teammate",
                        "person edit INDEX [-n NAME] [-p PHONE] [-e EMAIL] [-a ADDRESS] [-t TAG]…",
                        "person edit 1 -p 87654321 -e newmail@u.nus.edu -t"),
                new CommandSummary("Delete a teammate",
                        "person delete INDEX", "person delete 1"),
                new CommandSummary("List all teammates",
                        "person list", "person list"),
                new CommandSummary("Search for a teammate",
                        "person find KEYWORD [MORE_KEYWORDS]", "person find alex david"),
                new CommandSummary("Add a module for a teammate",
                        "module add INDEX MODULE_CODE DAY START_TIME END_TIME", "module add 1 CS2101 Thu 12:00 15:00"),
                new CommandSummary("Delete a module from a teammate",
                        "module delete INDEX MODULE_CODE", "module delete 1 CS2101"),
                new CommandSummary("Add a meeting",
                        "meeting add DATE START_TIME END_TIME", "meeting add 06-04-2025 12:00 15:00"),
                new CommandSummary("Delete a meeting",
                        "meeting delete INDEX", "meeting delete 1"),
                new CommandSummary("View help",
                        "help", "help"),
                new CommandSummary("Change week displayed",
                        "showdate DATE", "showdate 03-04-2025"),
                new CommandSummary("Clear all data", "clear", "clear"),
                new CommandSummary("Exit TeamSync", "exit", "exit"));
    }

}
