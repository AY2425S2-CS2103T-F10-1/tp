package syncsquad.teamsync.logic.commands;

import static java.util.Objects.requireNonNull;

import syncsquad.teamsync.commons.util.ToStringBuilder;
import syncsquad.teamsync.model.Model;

/**
 * Sets how many minutes before a meeting starts its reminder is shown, or turns reminders off.
 */
public class ReminderCommand extends Command {
    public static final String COMMAND_WORD = "reminder";
    public static final String OFF_KEYWORD = "off";
    public static final int MAX_LEAD_MINUTES = 1440;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a reminder the given number of minutes before each meeting starts, or turns reminders off\n"
            + "Parameters: MINUTES (1 to " + MAX_LEAD_MINUTES + ") or " + OFF_KEYWORD + "\n"
            + "Example: " + COMMAND_WORD + " 10";
    public static final String MESSAGE_SUCCESS = "Reminders will be shown %d minutes before each meeting";
    public static final String MESSAGE_OFF_SUCCESS = "Reminders turned off";

    private final int leadMinutes;

    /**
     * Creates a {@code ReminderCommand} that sets the reminder lead time to {@code leadMinutes}.
     * 0 turns reminders off.
     */
    public ReminderCommand(int leadMinutes) {
        assert leadMinutes >= 0 && leadMinutes <= MAX_LEAD_MINUTES;
        this.leadMinutes = leadMinutes;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setReminderLeadMinutes(leadMinutes);
        return new CommandResult(leadMinutes == 0 ? MESSAGE_OFF_SUCCESS : String.format(MESSAGE_SUCCESS, leadMinutes));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ReminderCommand)) {
            return false;
        }

        ReminderCommand otherReminderCommand = (ReminderCommand) other;
        return leadMinutes == otherReminderCommand.leadMinutes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("leadMinutes", leadMinutes)
                .toString();
    }
}
