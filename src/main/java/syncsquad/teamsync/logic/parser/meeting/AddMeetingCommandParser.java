package syncsquad.teamsync.logic.parser.meeting;

import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_START_END_TIME;
import static syncsquad.teamsync.logic.parser.CliSyntax.PREFIX_TITLE;

import java.time.LocalDate;
import java.time.LocalTime;

import syncsquad.teamsync.logic.commands.meeting.AddMeetingCommand;
import syncsquad.teamsync.logic.parser.ArgumentMultimap;
import syncsquad.teamsync.logic.parser.ArgumentTokenizer;
import syncsquad.teamsync.logic.parser.Parser;
import syncsquad.teamsync.logic.parser.ParserUtil;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.meeting.Title;

/**
 * Parses input arguments and creates a new AddMeetingCommand object
 */
public class AddMeetingCommandParser implements Parser<AddMeetingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddMeetingCommand
     * and returns an AddMeetingCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddMeetingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_TITLE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TITLE);

        String trimmedArgs = argMultimap.getPreamble().trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
        }

        String[] dateTimeSplit = trimmedArgs.split("\\s+");

        if (dateTimeSplit.length != 3) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMeetingCommand.MESSAGE_USAGE));
        }

        LocalDate date = ParserUtil.parseDate(dateTimeSplit[0]);
        LocalTime startTime = ParserUtil.parseTime(dateTimeSplit[1]);
        LocalTime endTime = ParserUtil.parseTime(dateTimeSplit[2]);

        if (!endTime.isAfter(startTime)) {
            throw new ParseException(MESSAGE_INVALID_START_END_TIME);
        }

        Title title = argMultimap.getValue(PREFIX_TITLE).isPresent()
                ? ParserUtil.parseTitle(argMultimap.getValue(PREFIX_TITLE).get())
                : Title.DEFAULT;

        Meeting meeting = new Meeting(date, startTime, endTime, title);
        return new AddMeetingCommand(meeting);
    }
}
