package syncsquad.teamsync.model;

import static java.util.Objects.requireNonNull;
import static syncsquad.teamsync.commons.util.AppUtil.checkArgument;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import syncsquad.teamsync.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    public static final int DEFAULT_REMINDER_LEAD_MINUTES = 15;

    private GuiSettings guiSettings = new GuiSettings();
    private Path addressBookFilePath = Paths.get("data" , "teamsync.json");
    private int reminderLeadMinutes = DEFAULT_REMINDER_LEAD_MINUTES;

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setAddressBookFilePath(newUserPrefs.getAddressBookFilePath());
        setReminderLeadMinutes(newUserPrefs.getReminderLeadMinutes());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        this.addressBookFilePath = addressBookFilePath;
    }

    public int getReminderLeadMinutes() {
        return reminderLeadMinutes;
    }

    /**
     * Sets how many minutes before a meeting starts its reminder is shown. 0 turns reminders off.
     */
    public void setReminderLeadMinutes(int reminderLeadMinutes) {
        checkArgument(reminderLeadMinutes >= 0, "Reminder lead time cannot be negative");
        this.reminderLeadMinutes = reminderLeadMinutes;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UserPrefs)) {
            return false;
        }

        UserPrefs otherUserPrefs = (UserPrefs) other;
        return guiSettings.equals(otherUserPrefs.guiSettings)
                && addressBookFilePath.equals(otherUserPrefs.addressBookFilePath)
                && reminderLeadMinutes == otherUserPrefs.reminderLeadMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath, reminderLeadMinutes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + addressBookFilePath);
        sb.append("\nReminder lead time (minutes) : " + reminderLeadMinutes);
        return sb.toString();
    }

}
