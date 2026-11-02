package syncsquad.teamsync.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static syncsquad.teamsync.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static syncsquad.teamsync.testutil.Assert.assertThrows;
import static syncsquad.teamsync.testutil.TypicalIndexes.INDEX_FIRST;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.model.module.Day;
import syncsquad.teamsync.model.person.Address;
import syncsquad.teamsync.model.person.Email;
import syncsquad.teamsync.model.person.Name;
import syncsquad.teamsync.model.person.Phone;
import syncsquad.teamsync.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_DAY = "monday";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_DAY = "MON";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class,
                MESSAGE_INVALID_INDEX, () -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseDay_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDay((String) null));
    }

    @Test
    public void parseDay_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDay(INVALID_DAY));
    }

    @Test
    public void parseDay_validValueWithoutWhitespace_returnsDay() throws Exception {
        Day expectedDay = new Day(VALID_DAY);
        assertEquals(expectedDay, ParserUtil.parseDay(VALID_DAY));
    }

    @Test
    public void parseDay_validValueWithWhitespace_returnsTrimmedDay() throws Exception {
        String dayWithWhitespace = WHITESPACE + VALID_DAY + WHITESPACE;
        Day expectedDay = new Day(VALID_DAY);
        assertEquals(expectedDay, ParserUtil.parseDay(dayWithWhitespace));
    }

    @Test
    public void parseDate_validDate_returnsDate() throws Exception {
        final String validDate = "18-11-2025";
        final String validDateWithLeadingZeros = "06-06-2025";
        final String validDateWithoutLeadingZero = "7-5-2025";
        final String validDateLeapYear = "29-02-2024";

        final LocalDate expectedValidDate = LocalDate.of(2025, 11, 18);
        final LocalDate expectedValidDateWithLeadingZeros = LocalDate.of(2025, 6, 6);
        final LocalDate expectedValidDateWithoutTrailingZero = LocalDate.of(2025, 5, 7);
        final LocalDate expectedValidDateLeapYear = LocalDate.of(2024, 2, 29);

        assertEquals(expectedValidDate, ParserUtil.parseDate(validDate));
        assertEquals(expectedValidDateWithLeadingZeros, ParserUtil.parseDate(validDateWithLeadingZeros));
        assertEquals(expectedValidDateWithoutTrailingZero, ParserUtil.parseDate(validDateWithoutLeadingZero));
        assertEquals(expectedValidDateLeapYear, ParserUtil.parseDate(validDateLeapYear));
    }

    @Test
    public void parseDate_invalidDate_throwsParseException() throws Exception {
        final String invalidDateFormatWithTextMonth = "24 Sep 2025";
        final String invalidDateFormatWithSlashes = "15/02/2025";
        final String invalidDateFormateWithYmd = "2025-11-19";
        final String invalidDateFormatDay = "32-11-2025";
        final String invalidDateFormatMonth = "12-13-2025";

        final String invalidInput30DayMonth = "31-04-2025";
        final String invalidInputFebruary = "30-02-2025";
        final String invalidInputNonLeapYear = "29-02-2025";

        // Exception message is invalid date format
        // Assertions.assertThrows() is used instead of the wrapper method as the exception message is tested
        Exception exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidDateFormatWithTextMonth));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_DATE_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidDateFormatWithSlashes));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_DATE_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidDateFormateWithYmd));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_DATE_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidDateFormatDay));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_DATE_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidDateFormatMonth));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_DATE_FORMAT);

        // Exception message is invalid date input
        // Assertions.assertThrows() is used instead of the wrapper method as the exception message is tested
        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidInput30DayMonth));
        assertEquals(exception.getMessage(),
                String.format(ParserUtil.DATE_MESSAGE_CONSTRAINTS, invalidInput30DayMonth));

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidInputFebruary));
        assertEquals(exception.getMessage(),
                String.format(ParserUtil.DATE_MESSAGE_CONSTRAINTS, invalidInputFebruary));

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseDate(invalidInputNonLeapYear));
        assertEquals(exception.getMessage(),
                String.format(ParserUtil.DATE_MESSAGE_CONSTRAINTS, invalidInputNonLeapYear));
    }

    @Test
    public void parseTime_validTime_returnsTime() throws Exception {
        final String validTime = "11:59";
        final String validTimeAfternoon = "22:50";
        final String validTimeWithoutLeadingZero = "2:34";
        final String validTimeWithLeadingZero = "01:00";

        final LocalTime expectedValidTime = LocalTime.of(11, 59);
        final LocalTime expectedValidTimeAfternoon = LocalTime.of(22, 50);
        final LocalTime expectedValidTimeWithoutLeadingZero = LocalTime.of(2, 34);
        final LocalTime expectedValidTimeWithLeadingZero = LocalTime.of(1, 0);

        assertEquals(expectedValidTime, ParserUtil.parseTime(validTime));
        assertEquals(expectedValidTimeAfternoon, ParserUtil.parseTime(validTimeAfternoon));
        assertEquals(expectedValidTimeWithoutLeadingZero, ParserUtil.parseTime(validTimeWithoutLeadingZero));
        assertEquals(expectedValidTimeWithLeadingZero, ParserUtil.parseTime(validTimeWithLeadingZero));
    }

    @Test
    public void parseTime_invalidTime_throwsParseException() throws Exception {
        final String invalidTimeWithText = "2 pm";
        final String invalidTimeWithoutColon = "1500";
        final String invalidTimeFormatHour = "24:59";
        final String invalidTimeFormatMinute = "05:65";

        // Exception message is invalid time format
        // Assertions.assertThrows() is used instead of the wrapper method as the exception message is tested
        Exception exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseTime(invalidTimeWithText));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_TIME_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseTime(invalidTimeWithoutColon));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_TIME_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseTime(invalidTimeFormatHour));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_TIME_FORMAT);

        exception = Assertions.assertThrows(ParseException.class, () -> ParserUtil
                .parseTime(invalidTimeFormatMinute));
        assertEquals(exception.getMessage(), ParserUtil.MESSAGE_INVALID_TIME_FORMAT);
    }
}
