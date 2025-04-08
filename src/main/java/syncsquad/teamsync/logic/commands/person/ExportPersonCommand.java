package syncsquad.teamsync.logic.commands.person;

import static java.util.Objects.requireNonNull;

import java.util.List;

import syncsquad.teamsync.commons.core.index.Index;
import syncsquad.teamsync.commons.util.ToStringBuilder;
import syncsquad.teamsync.logic.Messages;
import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.model.Model;
import syncsquad.teamsync.model.person.Person;

/**
 * Exports a person's data in a format that can be copied and imported.
 */
public class ExportPersonCommand extends PersonCommand {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_GROUP_WORD + " " + COMMAND_WORD
            + ": Exports a person's data in a format that can be copied and imported.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_GROUP_WORD + " " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EXPORT_PERSON_SUCCESS = "Exported person:\n"
        + COMMAND_GROUP_WORD + " " + AddPersonCommand.COMMAND_WORD + " %1$s";

    private final Index targetIndex;

    /**
     * Creates an ExportPersonCommand to export the specified person.
     */
    public ExportPersonCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToExport = lastShownList.get(targetIndex.getZeroBased());
        String exportString = personToExport.toExportString();
        return new CommandResult(String.format(MESSAGE_EXPORT_PERSON_SUCCESS, exportString));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportPersonCommand)) {
            return false;
        }

        ExportPersonCommand otherExportPersonCommand = (ExportPersonCommand) other;
        return targetIndex.equals(otherExportPersonCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
