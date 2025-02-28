package syncsquad.teamsync.viewmodel;

import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import syncsquad.teamsync.commons.core.LogsCenter;
import syncsquad.teamsync.logic.Logic;
import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;

public class MainViewModel {
    private final CommandBoxViewModel commandBoxViewModel;
    private final ResultDisplayViewModel resultDisplayViewModel;
    private final PersonListViewModel personListViewModel;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Logic logic;

    private BooleanProperty isShowingHelp = new SimpleBooleanProperty(false);
    private BooleanProperty isExiting = new SimpleBooleanProperty(false);

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
