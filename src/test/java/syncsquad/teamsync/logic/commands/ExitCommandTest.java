package syncsquad.teamsync.logic.commands;

import static syncsquad.teamsync.logic.commands.CommandTestUtil.assertCommandSuccess;
import static syncsquad.teamsync.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.model.Model;
import syncsquad.teamsync.model.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }
}
