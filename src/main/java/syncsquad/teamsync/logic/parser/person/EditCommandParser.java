package syncsquad.teamsync.logic.parser.person;

import static java.util.Objects.requireNonNull;
import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static syncsquad.teamsync.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static syncsquad.teamsync.logic.parser.CliSyntax.PREFIX_EMAIL;
import static syncsquad.teamsync.logic.parser.CliSyntax.PREFIX_NAME;
import static syncsquad.teamsync.logic.parser.CliSyntax.PREFIX_PHONE;
import static syncsquad.teamsync.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import syncsquad.teamsync.commons.core.index.Index;
import syncsquad.teamsync.logic.commands.person.EditCommand;
import syncsquad.teamsync.logic.commands.person.EditCommand.EditPersonDescriptor;
import syncsquad.teamsync.logic.parser.ArgumentMultimap;
import syncsquad.teamsync.logic.parser.ArgumentTokenizer;
import syncsquad.teamsync.logic.parser.Parser;
import syncsquad.teamsync.logic.parser.ParserUtil;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        args = args + " "; // Add a space to ensure all arguments are matched
        /*
         * Proof of correctness:
         * Given
         * (1) argument flags can only be matched if it has a following space (From
         * CliSyntax)
         * (2) all argument values will be trimmed during preprocessing (From ParserUtils)
         *
         * If the last section of the command is argument value, the added space will be
         * trimmed by (2). Then it will not affect the output
         *
         * If the last section of the command is argument flag
         * - Case 1: has space (`-c ` -> `-c  ` )
         * Argument will be matched, the additional space in arg value will be trimmed by
         * (2)
         * - Case 2: no space (`-c` -> `-c ` )
         * Argument will be matched, no additional space in arg value
         */

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if
     * {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be
     * parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
