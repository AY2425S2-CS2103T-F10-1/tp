package syncsquad.teamsync.controller;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonInfoCardController extends UiPart<Region> {

    private static final String FXML = "PersonInfoCard.fxml";

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
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;

    /**
     * Creates a {@code PersonInfoCard} with a given {@code Person}'s contact information.
     */
    public PersonInfoCardController(Person person) {
        super(FXML);
        this.person = person;

        phone.setText(person.getPhone().value);
        FontIcon phoneIcon = new FontIcon(Material2MZ.PHONE);
        phone.setGraphic(phoneIcon);

        address.setText(person.getAddress().value);
        FontIcon addressIcon = new FontIcon(Material2AL.HOME);
        address.setGraphic(addressIcon);

        email.setText(person.getEmail().value);
        FontIcon emailIcon = new FontIcon(Material2MZ.MAIL);
        email.setGraphic(emailIcon);
    }
}
