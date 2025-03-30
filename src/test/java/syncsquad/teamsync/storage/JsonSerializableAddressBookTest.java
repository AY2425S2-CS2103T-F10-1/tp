package syncsquad.teamsync.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static syncsquad.teamsync.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.commons.exceptions.IllegalValueException;
import syncsquad.teamsync.commons.util.JsonUtil;
import syncsquad.teamsync.model.AddressBook;
import syncsquad.teamsync.testutil.TypicalAddressBook;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path TYPICAL_MEETINGS_FILE = TEST_DATA_FOLDER.resolve("typicalMeetingsAddressBook.json");
    private static final Path INVALID_MEETING_FILE = TEST_DATA_FOLDER.resolve("invalidMeetingAddressBook.json");
    private static final Path DUPLICATE_MEETING_FILE = TEST_DATA_FOLDER.resolve("duplicateMeetingAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalAddressBook.getTypicalPersonsAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_typicalMeetingsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_MEETINGS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalMeetingsAddressBook = TypicalAddressBook.getTypicalMeetingsAddressBook();
        assertEquals(addressBookFromFile, typicalMeetingsAddressBook);
    }

    @Test
    public void toModelType_invalidMeetingFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_MEETING_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateMeetings_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_MEETING_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_MEETING,
                dataFromFile::toModelType);
    }

}
