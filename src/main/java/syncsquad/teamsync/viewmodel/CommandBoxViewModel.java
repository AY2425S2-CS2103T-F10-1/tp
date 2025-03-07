package syncsquad.teamsync.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;

/**
 * ViewModel for the command input box in the UI.
 * It binds the command input field to a StringProperty and provides
 * functionality to execute the command entered by the user.
 */
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

    /**
     * Executes the command entered in the command input field.
     *
     * @throws ParseException if there is an error parsing the command.
     * @throws CommandException if there is an error executing the command.
     */
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
