package syncsquad.teamsync.controller;

import java.time.LocalDate;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * An UI component that displays the date of a {@code Meeting}. This is the card used in
 * each root entry in the {@code MeetingTreeView}.
 */
public class MeetingRootCardController extends UiPart<Region> {

    private static final String FXML = "MeetingRootCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label titleLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;

    /**
     * Creates a {@code MeetingRootCardController} with the given
     * {@code Meeting} and index to display.
     *
     * @param meeting The meeting to display.
     * @param displayedIndex The index of the meeting in the list.
     */
    public MeetingRootCardController(Meeting meeting, int displayedIndex) {
        super(FXML);
        id.setText(displayedIndex + ". ");
        id.getStyleClass().add(Styles.TITLE_4);

        titleLabel.setText(meeting.getTitle().value);
        titleLabel.getStyleClass().add(Styles.TITLE_4);

        dateLabel.setText(meeting.getDateString());
        dateLabel.getStyleClass().add(Styles.TITLE_4);

        if (meeting.getDate().isBefore(LocalDate.now())) {
            dateLabel.getStyleClass().add(Styles.DANGER);
        } else if (meeting.getDate().isEqual(LocalDate.now())) {
            dateLabel.getStyleClass().add(Styles.WARNING);
        } else {
            dateLabel.getStyleClass().add(Styles.SUCCESS);
        }

        timeLabel.setText(meeting.getStartTimeString() + " to " + meeting.getEndTimeString());
        FontIcon endTimeIcon = new FontIcon(Material2AL.ACCESS_TIME);
        timeLabel.setGraphic(endTimeIcon);
    }
}
