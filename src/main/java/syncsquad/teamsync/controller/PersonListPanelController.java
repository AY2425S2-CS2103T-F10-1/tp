package syncsquad.teamsync.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.person.Person;
import syncsquad.teamsync.viewmodel.PersonListViewModel;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanelController extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanelController(PersonListViewModel viewModel) {
        super(FXML);
        personListView.setItems(viewModel.personListProperty());
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCardController(person, getIndex() + 1).getRoot());
            }
        }
    }

}
