package syncsquad.teamsync.logic.parser;

import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import syncsquad.teamsync.logic.commands.ReminderCommand;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ReminderCommand object
 */
public class ReminderCommandParser implements Parser<ReminderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReminderCommand
     * and returns a ReminderCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public ReminderCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.equalsIgnoreCase(ReminderCommand.OFF_KEYWORD)) {
            return new ReminderCommand(0);
        }
        if (!trimmedArgs.matches("\\d{1,4}")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReminderCommand.MESSAGE_USAGE));
        }
        int leadMinutes = Integer.parseInt(trimmedArgs);
        if (leadMinutes < 1 || leadMinutes > ReminderCommand.MAX_LEAD_MINUTES) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReminderCommand.MESSAGE_USAGE));
        }
        return new ReminderCommand(leadMinutes);
    }
}
