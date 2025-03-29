package syncsquad.teamsync.controller;

import java.util.HashSet;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;
import syncsquad.teamsync.model.person.Address;
import syncsquad.teamsync.model.person.Email;
import syncsquad.teamsync.model.person.Name;
import syncsquad.teamsync.model.person.Person;
import syncsquad.teamsync.model.person.Phone;
import syncsquad.teamsync.viewmodel.PersonListViewModel;

/**
 * Panel containing the {@code TreeView} of persons.
 * Each person has a root {@code TreeCell} containing their name and tags.
 * Then 2 children leaf items, one for their contact information and another for their modules.
 */
public class PersonTreeViewController extends UiPart<Region> {
    private static final String FXML = "PersonTreeView.fxml";

    //TreeView requires a root node to be set, so we create a dummy root node
    private static final Person ROOT_DUMMY_PERSON = new Person(
        new Name("People"), new Phone("777"), new Email("root@root.com"),
        new Address("dummy"), new HashSet<>(), new HashSet<>());
    private static final TreeItem<Person> ROOT_TREE_ITEM = new TreeItem<>(ROOT_DUMMY_PERSON);

    @FXML
    private TreeView<Person> personTreeView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonTreeViewController(PersonListViewModel viewModel) {
        super(FXML);

        personTreeView.setRoot(ROOT_TREE_ITEM);
        ROOT_TREE_ITEM.setExpanded(true);

        viewModel.personListProperty().forEach(person -> {
            TreeItem<Person> personItem = new TreeItem<>(person);
            personItem.getChildren().add(new TreeItem<>(person)); // Contact Info
            personItem.getChildren().add(new TreeItem<>(person)); // Modules
            ROOT_TREE_ITEM.getChildren().add(personItem);
            personItem.setExpanded(true);
        });

        viewModel.personListProperty().addListener((ListChangeListener<Person>) change -> {
            ROOT_TREE_ITEM.getChildren().clear();
            viewModel.personListProperty().forEach(person -> {
                TreeItem<Person> personItem = new TreeItem<>(person);
                personItem.getChildren().add(new TreeItem<>(person)); // Contact Info
                personItem.getChildren().add(new TreeItem<>(person)); // Modules
                ROOT_TREE_ITEM.getChildren().add(personItem);
                personItem.setExpanded(true);
            });
        });

        personTreeView.setCellFactory(treeView -> new PersonTreeCell());
    }

    /**
     * Custom {@code TreeCell} implementation for displaying {@code Person} objects.
     * DO NOT USE ATLANTAFX STYLES HERE. Learnt that the hard way.
     */
    private static class PersonTreeCell extends TreeCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            //lines 74 to 78 must not be modified
            super.updateItem(person, empty);
            if (empty || person == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            if (getTreeItem() == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            if (person == ROOT_DUMMY_PERSON) {
                setGraphic(new PersonDummyRootCardController().getRoot());
            } else {
                setText(null);
                if (!getTreeItem().isLeaf()) {
                    setGraphic(new PersonRootCardController(person,
                            getTreeItem().getParent().getChildren().stream()
                                    .map(TreeItem::getValue).toList().indexOf(person) + 1).getRoot());
                } else {
                    if (getTreeItem().getParent().getChildren().indexOf(getTreeItem()) == 0) {
                        setGraphic(new PersonInfoCardController(person).getRoot());
                    } else {
                        setGraphic(new PersonModulesCardController(person).getRoot());
                    }
                }
            }
        }
    }
}
