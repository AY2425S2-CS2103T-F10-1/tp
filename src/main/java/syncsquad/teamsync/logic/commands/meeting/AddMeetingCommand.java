package syncsquad.teamsync.logic.commands.meeting;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import syncsquad.teamsync.commons.util.ToStringBuilder;
import syncsquad.teamsync.logic.Messages;
import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.model.Model;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * Adds a meeting to the address book.
 */
public class AddMeetingCommand extends MeetingCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_GROUP_WORD + " " + COMMAND_WORD
            + ": Creates a meeting with the specified date, start and end time\n"
            + "Parameters: DATE START_TIME END_TIME\n"
            + "Example: " + COMMAND_GROUP_WORD + " " + COMMAND_WORD + " 15-11-2025 11:00 15:00";

    public static final String MESSAGE_SUCCESS = "Meeting added with the following details: %1$s";
    public static final String MESSAGE_DUPLICATE_MEETING = "This meeting already exists in the address book";
    public static final String MESSAGE_OVERLAP_MEETING = "There is another meeting during this time period already. ";

    private final Meeting toAdd;

    /**
     * Initialises a AddMeetingCommand object to add a meeting
     *
     * @param meeting The meeting to add
     */
    public AddMeetingCommand(Meeting meeting) {
        requireNonNull(meeting);
        toAdd = meeting;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasMeeting(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEETING);
        }

        ObservableList<Meeting> meetingList = model.getMeetingList();
        // check for overlapping meetings
        if (hasOverlap(meetingList)) {
            throw new CommandException(MESSAGE_OVERLAP_MEETING);
        }

        model.addMeeting(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    /**
     * Checks if the meeting to be added overlaps with existing meetings
     *
     * @param meetingList List of meetings to check for overlap
     * @return true if there is an overlap, false otherwise
     */
    public boolean hasOverlap(ObservableList<Meeting> meetingList) {
        requireNonNull(meetingList);
        return meetingList.stream().anyMatch(toAdd::isOverlapping);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddMeetingCommand)) {
            return false;
        }

        // state check
        AddMeetingCommand otherAddMeetingCommand = (AddMeetingCommand) other;
        return toAdd.equals(otherAddMeetingCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

}
