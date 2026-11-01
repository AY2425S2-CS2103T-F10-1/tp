package syncsquad.teamsync.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import syncsquad.teamsync.commons.core.GuiSettings;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns how many minutes before a meeting starts its reminder is shown, or 0 if reminders are off.
     */
    int getReminderLeadMinutes();

    /**
     * Sets how many minutes before a meeting starts its reminder is shown. 0 turns reminders off.
     */
    void setReminderLeadMinutes(int reminderLeadMinutes);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if a meeting with the same date, start time and end time as {@code meeting}
     * exists in the address book.
     */
    boolean hasMeeting(Meeting meeting);

    /**
     * Adds the given meeting.
     * {@code meeting} must not already exist in the address book.
     */
    void addMeeting(Meeting meeting);

    /**
     * Deletes the given meeting.
     * The meeting must exist in the address book.
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Returns the meetings list as a string formatted for display to the user
     */
    String displayMeetingsString();


    /** Returns an unmodifiable view of the list of meetings */
    ObservableList<Meeting> getMeetingList();

    TimetableWeek getCurrentWeek();

    void setCurrentWeek(TimetableWeek week);
}
