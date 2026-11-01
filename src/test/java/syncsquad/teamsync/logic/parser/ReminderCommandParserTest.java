package syncsquad.teamsync.logic.parser;

import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static syncsquad.teamsync.logic.parser.CommandParserTestUtil.assertParseFailure;
import static syncsquad.teamsync.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.logic.commands.ReminderCommand;

public class ReminderCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReminderCommand.MESSAGE_USAGE);

    private final ReminderCommandParser parser = new ReminderCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " 10 ", new ReminderCommand(10));
        assertParseSuccess(parser, "1", new ReminderCommand(1));
        assertParseSuccess(parser, "1440", new ReminderCommand(1440));
        assertParseSuccess(parser, " Off", new ReminderCommand(0));
    }

    @Test
    public void parse_invalidArgs_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1441", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-5", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "10 min", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "99999999999", MESSAGE_INVALID_FORMAT);
    }
}
