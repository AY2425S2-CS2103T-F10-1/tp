package syncsquad.teamsync.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import syncsquad.teamsync.viewmodel.ResultDisplayViewModel;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplayController extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    public ResultDisplayController(ResultDisplayViewModel viewModel) {
        super(FXML);

        resultDisplay.textProperty().bind(viewModel.feedbackToUserProperty());
    }
}
