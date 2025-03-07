package syncsquad.teamsync.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;
import syncsquad.teamsync.model.person.Person;

/**
 * ViewModel class for managing a list of Person objects.
 * This class provides a read-only property for the list of persons.
 */
public class PersonListViewModel {
    private final ReadOnlyListWrapper<Person> personList;

    public PersonListViewModel(ObservableList<Person> personList) {
        this.personList = new ReadOnlyListWrapper<Person>(personList);
    }

    public ReadOnlyListProperty<Person> personListProperty() {
        return personList.getReadOnlyProperty();
    }
}
