package syncsquad.teamsync.controller;

import java.util.Comparator;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.person.Person;

/**
 * An UI component that displays the name and tags of a {@code Person}. This is the card used in
 * each root entry in the {@code PersonTreeView}.
 */
public class PersonRootCardController extends UiPart<Region> {

    private static final String FXML = "PersonRootCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonRootCardController(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        id.getStyleClass().add(Styles.TITLE_4);

        name.setText(person.getName().fullName);
        name.getStyleClass().add(Styles.TITLE_4);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.getStyleClass().addAll(
                        Styles.SUCCESS
                    );
                    FontIcon tagIcon = new FontIcon(Material2AL.LABEL);
                    tagLabel.setGraphic(tagIcon);
                    cardPane.getChildren().add(tagLabel);
                });
    }
}
