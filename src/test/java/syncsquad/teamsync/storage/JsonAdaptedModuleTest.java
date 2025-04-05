package syncsquad.teamsync.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static syncsquad.teamsync.testutil.Assert.assertThrows;
import static syncsquad.teamsync.testutil.TypicalAddressBook.CS2103T_MODULE;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import syncsquad.teamsync.commons.exceptions.IllegalValueException;
import syncsquad.teamsync.model.module.Day;
import syncsquad.teamsync.model.module.Module;
import syncsquad.teamsync.model.module.ModuleCode;

public class JsonAdaptedModuleTest {
    private static final String VALID_MODULE_CODE = "CS2103T";
    private static final String VALID_DAY = "FRI";
    private static final String VALID_START_TIME = "14:00";
    private static final String VALID_END_TIME = "16:00";

    @Test
    public void constructor_validFields_success() throws IllegalValueException {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                VALID_MODULE_CODE, VALID_DAY, VALID_START_TIME, VALID_END_TIME);
        assertEquals(CS2103T_MODULE, adaptedModule.toModelType());
    }

    @Test
    public void constructor_fromModule_success() throws IllegalValueException {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(CS2103T_MODULE);
        assertEquals(CS2103T_MODULE, adaptedModule.toModelType());
    }

    @Test
    public void toModelType_validFields_returnsModule() throws Exception {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                VALID_MODULE_CODE, VALID_DAY, VALID_START_TIME, VALID_END_TIME);
        Module module = adaptedModule.toModelType();

        assertEquals(new ModuleCode(VALID_MODULE_CODE), module.getModuleCode());
        assertEquals(new Day(VALID_DAY), module.getDay());
        assertEquals(LocalTime.parse(VALID_START_TIME), module.getStartTime());
        assertEquals(LocalTime.parse(VALID_END_TIME), module.getEndTime());
    }

    @Test
    public void toModelType_nullModuleCode_throwsIllegalValueException() {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                null, VALID_DAY, VALID_START_TIME, VALID_END_TIME);
        assertThrows(IllegalValueException.class, adaptedModule::toModelType);
    }

    @Test
    public void toModelType_invalidModuleCode_throwsIllegalValueException() {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                "INVALID", VALID_DAY, VALID_START_TIME, VALID_END_TIME);
        assertThrows(IllegalValueException.class, adaptedModule::toModelType);
    }

    @Test
    public void toModelType_nullDay_throwsIllegalValueException() {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                VALID_MODULE_CODE, null, VALID_START_TIME, VALID_END_TIME);
        assertThrows(IllegalValueException.class, adaptedModule::toModelType);
    }

    @Test
    public void toModelType_invalidDay_throwsIllegalValueException() {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                VALID_MODULE_CODE, "INVALID", VALID_START_TIME, VALID_END_TIME);
        assertThrows(IllegalValueException.class, adaptedModule::toModelType);
    }

    @Test
    public void toModelType_nullStartTime_throwsIllegalValueException() {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                VALID_MODULE_CODE, VALID_DAY, null, VALID_END_TIME);
        assertThrows(IllegalValueException.class, adaptedModule::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        JsonAdaptedModule adaptedModule = new JsonAdaptedModule(
                VALID_MODULE_CODE, VALID_DAY, VALID_START_TIME, null);
        assertThrows(IllegalValueException.class, adaptedModule::toModelType);
    }
}
