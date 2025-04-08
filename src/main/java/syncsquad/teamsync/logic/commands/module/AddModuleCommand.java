package syncsquad.teamsync.logic.commands.module;

import static java.util.Objects.requireNonNull;
import static syncsquad.teamsync.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import syncsquad.teamsync.commons.core.index.Index;
import syncsquad.teamsync.commons.util.ToStringBuilder;
import syncsquad.teamsync.logic.Messages;
import syncsquad.teamsync.logic.commands.CommandResult;
import syncsquad.teamsync.logic.commands.exceptions.CommandException;
import syncsquad.teamsync.model.Model;
import syncsquad.teamsync.model.module.Module;
import syncsquad.teamsync.model.person.Person;

/**
 * Adds a module to an existing person in the address book.
 */
public class AddModuleCommand extends ModuleCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_GROUP_WORD + " " + COMMAND_WORD
            + ": Adds the module to the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "MODULE_CODE "
            + "DAY "
            + "START_TIME (HH:MM) "
            + "END_TIME (HH:MM) \n"
            + "Example: " + COMMAND_GROUP_WORD + " " + COMMAND_WORD + " 1 "
            + "CS2103T FRI 16:00 18:00";

    public static final String MESSAGE_SUCCESS = "Added Module to Person: %1$s";
    public static final String MESSAGE_DUPLICATE_MODULE = "This module already exists for the person.";
    public static final String MESSAGE_OVERLAP_MODULE = "This person already has another module during this period.";

    private final Index index;
    private final Module module;

    /**
     * Initialises an AddModuleCommand object to add a module
     *
     * @param index  of the person in the filtered person list to edit
     * @param module module to add to the person
     */
    public AddModuleCommand(Index index, Module module) {
        requireNonNull(index);
        requireNonNull(module);

        this.index = index;
        this.module = module;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        if (personToEdit.getModules().stream().anyMatch(module::isSameModule)) {
            throw new CommandException(MESSAGE_DUPLICATE_MODULE);
        }
        Set<Module> moduleSet = new HashSet<>(personToEdit.getModules());

        // check for overlapping modules
        if (hasOverlap(moduleSet)) {
            throw new CommandException(MESSAGE_OVERLAP_MODULE);
        }

        moduleSet.add(module);
        Person newPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getAddress(), personToEdit.getTags(), moduleSet);
        model.setPerson(personToEdit, newPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(newPerson)));
    }

    /**
     * Checks if the module to be added overlaps with existing modules
     *
     * @param moduleSet Set of modules to check for overlap
     * @return true if there is an overlap, false otherwise
     */
    public boolean hasOverlap(Set<Module> moduleSet) {
        requireNonNull(moduleSet);
        return moduleSet.stream().anyMatch(module::isOverlapping);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddModuleCommand)) {
            return false;
        }

        AddModuleCommand otherAddModuleCommand = (AddModuleCommand) other;
        return index.equals(otherAddModuleCommand.index)
                && module.equals(otherAddModuleCommand.module);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("module", module)
                .toString();
    }
}
