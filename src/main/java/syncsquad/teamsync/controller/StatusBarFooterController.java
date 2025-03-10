package syncsquad.teamsync.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooterController extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label saveLocationStatus;

    /**
     * Creates a {@code StatusBarFooter} with the given {@code Path}.
     */
    public StatusBarFooterController(Path saveLocation) {
        super(FXML);
        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());
    }

}
