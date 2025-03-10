package syncsquad.teamsync.model;

import java.nio.file.Path;

import syncsquad.teamsync.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getAddressBookFilePath();

}
