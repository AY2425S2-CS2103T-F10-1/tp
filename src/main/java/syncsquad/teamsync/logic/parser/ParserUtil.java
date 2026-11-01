package syncsquad.teamsync.logic.parser;

import static java.util.Objects.requireNonNull;
import static syncsquad.teamsync.logic.Messages.MESSAGE_INVALID_START_END_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import syncsquad.teamsync.commons.core.index.Index;
import syncsquad.teamsync.commons.util.StringUtil;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.model.meeting.Title;
import syncsquad.teamsync.model.module.Day;
import syncsquad.teamsync.model.module.Module;
import syncsquad.teamsync.model.module.ModuleCode;
import syncsquad.teamsync.model.person.Address;
import syncsquad.teamsync.model.person.Email;
import syncsquad.teamsync.model.person.Name;
import syncsquad.teamsync.model.person.Phone;
import syncsquad.teamsync.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_DATE_FORMAT = "Invalid date format: Date should be in dd-mm-yyyy format";
    public static final String MESSAGE_INVALID_TIME_FORMAT = "Invalid time format: Time should be in HH:mm format";
    public static final String MESSAGE_DUPLICATE_MODULE = "Duplicate modules found for the person.";
    public static final String MESSAGE_OVERLAPPING_MODULE = "Overlapping modules found for the person.";

    /** Messages for invalid date and time input **/
    public static final String DATE_MESSAGE_CONSTRAINTS = "Invalid date input: %1$s is not a valid date.";
    public static final String TIME_MESSAGE_CONSTRAINTS = "Invalid time input: %1$s is not a valid time input.";

    /** Regex to check if the date and time input are of the correct format**/
    // Regex to check for dates of the form dd-mm-yyyy or dd-mm
    // Single digit day and month are allowed
    public static final String DATE_VALIDATION_REGEX = "^(?:0?[1-9]|[12][0-9]|3[01])-(?:0?[1-9]|1[0-2])(?:-\\d{4})?$";
    // Regex to check if time is of the form HH:mm
    public static final String TIME_VALIDATION_REGEX = "^(?:[0-9]|[01]\\d|2[0-3]):[0-5]\\d$";

    /** Parsers for date and time input **/
    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("[d-M-u]" + "[d-M]"))
            .parseCaseInsensitive()
            .parseDefaulting(ChronoField.YEAR, LocalDateTime.now().getYear())
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("H:mm"))
            .parseCaseInsensitive()
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero
     *                        unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String title} into a {@code Title}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code title} is invalid.
     */
    public static Title parseTitle(String title) throws ParseException {
        requireNonNull(title);
        String trimmedTitle = title.trim();
        if (!Title.isValidTitle(trimmedTitle)) {
            throw new ParseException(Title.MESSAGE_CONSTRAINTS);
        }
        return new Title(trimmedTitle);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String day} into a {@code Day}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code day} is invalid.
     */
    public static Day parseDay(String day) throws ParseException {
        requireNonNull(day);
        String trimmedDay = day.trim();
        if (!Day.isValidDay(trimmedDay)) {
            throw new ParseException(Day.MESSAGE_CONSTRAINTS);
        }
        return new Day(trimmedDay);
    }

    /**
     * Parses a {@code String moduleCode} into a {@code ModuleCode}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code moduleCode} is invalid.
     */
    public static ModuleCode parseModuleCode(String moduleCode) throws ParseException {
        requireNonNull(moduleCode);
        String trimmedModuleCode = moduleCode.trim();
        if (!ModuleCode.isValidModuleCode(trimmedModuleCode)) {
            throw new ParseException(ModuleCode.MESSAGE_CONSTRAINTS);
        }
        return new ModuleCode(trimmedModuleCode);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String module} into a {@code Module}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code module} is invalid.
     */
    public static Module parseModule(String module) throws ParseException {
        requireNonNull(module);
        String trimmedModule = module.trim();
        final Matcher matcher = Module.VALIDATION_REGEX.matcher(trimmedModule);
        if (!matcher.matches()) {
            throw new ParseException(String.format(Module.MESSAGE_CONSTRAINTS, trimmedModule));
        }

        ModuleCode moduleCode = parseModuleCode(matcher.group("moduleCode"));
        Day day = parseDay(matcher.group("day"));
        LocalTime startTime = parseTime(matcher.group("startTime"));
        LocalTime endTime = parseTime(matcher.group("endTime"));

        if (!endTime.isAfter(startTime)) {
            throw new ParseException(MESSAGE_INVALID_START_END_TIME);
        }

        return new Module(moduleCode, day, startTime, endTime);
    }

    /**
     * Parses {@code Collection<String> modules} into a {@code Set<Module>}.
     */
    public static Set<Module> parseModules(Collection<String> modules) throws ParseException {
        requireNonNull(modules);
        final Set<Module> moduleSet = new HashSet<>();
        for (String moduleString : modules) {
            Module module = parseModule(moduleString);
            if (moduleSet.stream().anyMatch(module::isSameModule)) {
                throw new ParseException(MESSAGE_DUPLICATE_MODULE);
            }
            if (moduleSet.stream().anyMatch(module::isOverlapping)) {
                throw new ParseException(MESSAGE_OVERLAPPING_MODULE);
            }
            moduleSet.add(module);
        }
        return moduleSet;
    }

    /**
     * Parses a {@code String date} into a {@code LocalDate}.
     *
     * @throws ParseException if the given {@code date} is in an invalid date format or is an invalid date input.
     */
    public static LocalDate parseDate(String date) throws ParseException {
        if (!isValidDate(date)) {
            throw new ParseException(MESSAGE_INVALID_DATE_FORMAT);
        }
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(String.format(DATE_MESSAGE_CONSTRAINTS, date));
        }
    }

    /**
     * Parses a {@code String time} into a {@code LocalTime}.
     *
     * @throws ParseException if the given {@code time} is in an invalid time format or is an invalid time input.
     */
    public static LocalTime parseTime(String time) throws ParseException {
        if (!isValidTime(time)) {
            throw new ParseException(MESSAGE_INVALID_TIME_FORMAT);
        }
        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(String.format(TIME_MESSAGE_CONSTRAINTS, time));
        }
    }

    /**
     * Returns if a given string is a valid date.
     */
    private static boolean isValidDate(String test) {
        assert test != null;
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Returns if a given string is a valid time.
     */
    private static boolean isValidTime(String test) {
        assert test != null;
        return test.matches(TIME_VALIDATION_REGEX);
    }
}
