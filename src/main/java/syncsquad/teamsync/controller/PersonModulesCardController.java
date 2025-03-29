package syncsquad.teamsync.controller;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonModulesCardController extends UiPart<Region> {

    private static final String FXML = "PersonModulesCard.fxml";

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
    private Label modules;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonModulesCardController(Person person) {
        super(FXML);
        this.person = person;

        StringBuilder moduleList = new StringBuilder();
        person.getModules().forEach(moduleList::append);
        modules.setText(moduleList.toString());
        FontIcon moduleIcon = new FontIcon(Material2AL.BOOK);
        modules.setGraphic(moduleIcon);
    }
}
