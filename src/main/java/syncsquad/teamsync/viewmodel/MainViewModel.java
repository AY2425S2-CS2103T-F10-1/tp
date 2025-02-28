package syncsquad.teamsync.viewmodel;

import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import syncsquad.teamsync.commons.core.LogsCenter;
import syncsquad.teamsync.logic.Logic;
import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;

/**
 * The MainViewModel class serves as the ViewModel for the main application window.
 * It holds references to various sub-view models and manages the execution of commands.
 *
 * <p>It contains the following sub-view models:
 * <ul>
 *   <li>{@link CommandBoxViewModel} - Manages the command input box.</li>
 *   <li>{@link ResultDisplayViewModel} - Manages the display of command results.</li>
 *   <li>{@link PersonListViewModel} - Manages the display of the list of persons.</li>
 * </ul>
 *
 * <p>The main responsibilities of this class include:
 * <ul>
 *   <li>Executing commands through the {@link Logic} component.</li>
 *   <li>Updating the result display based on command execution results.</li>
 *   <li>Setting flags for showing help and exiting the application based on command results.</li>
 * </ul>
 *
 * @see syncsquad.teamsync.logic.Logic
 */
public class MainViewModel {
    private final CommandBoxViewModel commandBoxViewModel;
    private final ResultDisplayViewModel resultDisplayViewModel;
    private final PersonListViewModel personListViewModel;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Logic logic;

    private BooleanProperty isShowingHelp = new SimpleBooleanProperty(false);
    private BooleanProperty isExiting = new SimpleBooleanProperty(false);

    /**
     * Represents the ViewModel for the main view of the application.
     * It acts as a bridge between the UI and the logic component.
     *
     * @param logic The logic component that handles the application's operations.
     */
    public MainViewModel(Logic logic) {
        this.logic = logic;
        this.commandBoxViewModel = new CommandBoxViewModel(this::executeCommand);
        this.resultDisplayViewModel = new ResultDisplayViewModel();
        this.personListViewModel = new PersonListViewModel(logic.getFilteredPersonList());
    }

    public CommandBoxViewModel getCommandBoxViewModel() {
        return commandBoxViewModel;
    }

    public ResultDisplayViewModel getResultDisplayViewModel() {
        return resultDisplayViewModel;
    }

    public PersonListViewModel getPersonListViewModel() {
        return personListViewModel;
    }

    public BooleanProperty getIsShowingHelpProperty() {
        return isShowingHelp;
    }

    public BooleanProperty getIsExiting() {
        return isExiting;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see syncsquad.teamsync.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplayViewModel.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                isShowingHelp.set(true);
            }

            if (commandResult.isExit()) {
                isExiting.set(true);
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplayViewModel.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
