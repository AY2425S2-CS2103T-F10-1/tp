package syncsquad.teamsync.logic.commands;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import syncsquad.teamsync.commons.core.GuiSettings;
import syncsquad.teamsync.model.Model;
import syncsquad.teamsync.model.ReadOnlyAddressBook;
import syncsquad.teamsync.model.ReadOnlyUserPrefs;
import syncsquad.teamsync.model.TimetableWeek;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.person.Person;

/**
 * A default model stub that has all methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public int getReminderLeadMinutes() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setReminderLeadMinutes(int reminderLeadMinutes) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getAddressBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasMeeting(Meeting meeting) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addMeeting(Meeting meeting) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        throw new AssertionError("This method should not be called.");
    }

    public String displayMeetingsString() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Meeting> getMeetingList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public TimetableWeek getCurrentWeek() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setCurrentWeek(TimetableWeek week) {
        throw new AssertionError("This method should not be called.");
    }

}
