package syncsquad.teamsync.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import syncsquad.teamsync.commons.core.index.Index;
import syncsquad.teamsync.logic.Messages;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.model.Model;
import syncsquad.teamsync.model.meeting.Meeting;

/**
 * Edits a meeting identified using its displayed index from the address book.
 */
public class EditMeetingCommand extends Command {
    public static final String COMMAND_WORD = "editmeeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the meeting identified by the index number used in the displayed meeting list\n"
            + "Parameters: INDEX (must be a positive integer) DATE START_TIME END_TIME\n"
            + "Example: " + COMMAND_WORD + " 1 15-11-2025 11:00 15:00";

    public static final String MESSAGE_EDIT_MEETING_SUCCESS = "Edited Meeting: %1$s";
    public static final String MESSAGE_DUPLICATE_MEETING = "This meeting already exists in the address book. ";
    public static final String MESSAGE_OVERLAP_MEETING = "There is another meeting during this time period already. ";

    private final Index targetIndex;
    private final Meeting newMeeting;

    /**
     * Creates a EditMeetingCommand to edit the meeting at the specified {@code targetIndex}
     */
    public EditMeetingCommand(Index targetIndex, Meeting newMeeting) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.newMeeting = newMeeting;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Meeting> lastShownList = model.getMeetingList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
        }

        // checks for duplicate meetings
        if (model.hasMeeting(newMeeting)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEETING);
        }

        ObservableList<Meeting> meetingList = model.getMeetingList();
        // check for overlapping meetings
        if (hasOverlap(meetingList)) {
            throw new CommandException(MESSAGE_OVERLAP_MEETING);
        }

        Meeting meetingToEdit = lastShownList.get(targetIndex.getZeroBased());
        model.editMeeting(meetingToEdit, this.newMeeting);
        return new CommandResult(String.format(MESSAGE_EDIT_MEETING_SUCCESS, meetingToEdit));
    }

    /**
     * Checks if the new meeting overlaps with existing meetings
     */
    public boolean hasOverlap(ObservableList<Meeting> meetingList) {
        requireNonNull(meetingList);
        return meetingList.stream().anyMatch(newMeeting::isOverlap);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditMeetingCommand)) {
            return false;
        }

        // state check
        EditMeetingCommand otherEditMeetingCommand = (EditMeetingCommand) other;
        return targetIndex.equals(otherEditMeetingCommand.targetIndex);
    }

}
