package syncsquad.teamsync.model;

import javafx.collections.ObservableList;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the meetings list.
     * This list will not contain any duplicate meetings.
     */
    ObservableList<Meeting> getMeetingList();

}
