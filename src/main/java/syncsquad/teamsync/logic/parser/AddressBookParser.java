package syncsquad.teamsync.logic.parser;

import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static syncsquad.teamsync.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import syncsquad.teamsync.commons.core.LogsCenter;
import syncsquad.teamsync.logic.commands.ClearCommand;
import syncsquad.teamsync.logic.commands.Command;
import syncsquad.teamsync.logic.commands.ExitCommand;
import syncsquad.teamsync.logic.commands.HelpCommand;
import syncsquad.teamsync.logic.commands.ReminderCommand;
import syncsquad.teamsync.logic.commands.ShowDateCommand;
import syncsquad.teamsync.logic.commands.meeting.MeetingCommand;
import syncsquad.teamsync.logic.commands.module.ModuleCommand;
import syncsquad.teamsync.logic.commands.person.PersonCommand;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.logic.parser.meeting.MeetingCommandsParser;
import syncsquad.teamsync.logic.parser.module.ModuleCommandsParser;
import syncsquad.teamsync.logic.parser.person.PersonCommandsParser;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput);
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        //Change command word to lower case to make it case-insensitive
        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level
        // (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case PersonCommand.COMMAND_GROUP_WORD:
            return new PersonCommandsParser().parse(arguments);

        case ModuleCommand.COMMAND_GROUP_WORD:
            return new ModuleCommandsParser().parse(arguments);

        case MeetingCommand.COMMAND_GROUP_WORD:
            return new MeetingCommandsParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ShowDateCommand.COMMAND_WORD:
            return new ShowDateCommandParser().parse(arguments);

        case ReminderCommand.COMMAND_WORD:
            return new ReminderCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
