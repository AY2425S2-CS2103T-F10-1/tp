package syncsquad.teamsync.controller;

import java.util.Comparator;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCardController extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label modules;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCardController(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        id.getStyleClass().add(Styles.TITLE_2);

        name.setText(person.getName().fullName);
        name.getStyleClass().add(Styles.TITLE_2);

        phone.setText(person.getPhone().value);
        FontIcon phoneIcon = new FontIcon(Material2MZ.PHONE);
        phone.setGraphic(phoneIcon);

        address.setText(person.getAddress().value);
        FontIcon addressIcon = new FontIcon(Material2AL.HOME);
        address.setGraphic(addressIcon);

        email.setText(person.getEmail().value);
        FontIcon emailIcon = new FontIcon(Material2MZ.MAIL);
        email.setGraphic(emailIcon);

        StringBuilder moduleList = new StringBuilder();
        person.getModules().forEach(moduleList::append);
        modules.setText(moduleList.toString());
        FontIcon moduleIcon = new FontIcon(Material2AL.BOOK);
        modules.setGraphic(moduleIcon);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.getStyleClass().addAll(
                            Styles.TEXT_CAPTION,
                            Styles.BG_SUCCESS_EMPHASIS,
                            Styles.TEXT_ON_EMPHASIS
                    );
                    tagLabel.setPadding(new Insets(2));
                    tags.getChildren().add(tagLabel);
                });
    }
}
