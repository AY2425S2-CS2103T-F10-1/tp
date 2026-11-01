package syncsquad.teamsync.model;

import java.nio.file.Path;

import syncsquad.teamsync.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getAddressBookFilePath();

    /**
     * Returns how many minutes before a meeting starts its reminder is shown, or 0 if reminders are off.
     */
    int getReminderLeadMinutes();

}
