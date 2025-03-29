package syncsquad.teamsync.controller;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * A card for the dummy root in the {@code PersonTreeView}.
 */
public class PersonDummyRootCardController extends UiPart<Region> {

    private static final String FXML = "PersonDummyRootCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label title;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonDummyRootCardController() {
        super(FXML);
        title.getStyleClass().addAll(Styles.TITLE_3);
        FontIcon peopleIcon = new FontIcon(Material2MZ.PEOPLE);
        title.setGraphic(peopleIcon);
    }
}
