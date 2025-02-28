package syncsquad.teamsync.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;

public class CommandBoxViewModel {
    private final CommandExecutor commandExecutor;
    private final StringProperty commandInput = new SimpleStringProperty();

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBoxViewModel(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }


    /**
     * Returns the StringProperty representing the command input.
     *
     * @return the StringProperty of the command input.
     */
    public StringProperty commandInputProperty() {
        return commandInput;
    }

    public void executeCommand() throws ParseException, CommandException {
        String commandText = commandInput.get().trim();
        if (commandText.equals("")) {
            return;
        }

        commandExecutor.execute(commandText);
        commandInput.set("");
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see syncsquad.teamsync.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }
}
