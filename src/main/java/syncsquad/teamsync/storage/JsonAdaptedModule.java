package syncsquad.teamsync.storage;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import syncsquad.teamsync.commons.exceptions.IllegalValueException;
import syncsquad.teamsync.model.module.Day;
import syncsquad.teamsync.model.module.Module;
import syncsquad.teamsync.model.module.ModuleCode;

/**
 * Jackson-friendly version of {@link Module}.
 */
class JsonAdaptedModule {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Module's %s field is missing!";

    private final String moduleCode;
    private final String day;
    private final String startTime;
    private final String endTime;

    /**
     * Constructs a {@code JsonAdaptedModule} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleCode") String moduleCode, @JsonProperty("day") String day,
                             @JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime) {
        this.moduleCode = moduleCode;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Converts a given {@code Module} into this class for Jackson use.
     */
    public JsonAdaptedModule(Module source) {
        moduleCode = source.getModuleCode().code;
        day = source.getDay().day;
        startTime = source.getStartTime().toString();
        endTime = source.getEndTime().toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Module toModelType() throws IllegalValueException {

        if (moduleCode == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, ModuleCode.class.getSimpleName()));
        }
        if (!ModuleCode.isValidModuleCode(moduleCode)) {
            throw new IllegalValueException(ModuleCode.MESSAGE_CONSTRAINTS);
        }
        final ModuleCode moduleCode = new ModuleCode(this.moduleCode);

        if (day == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Day.class.getSimpleName()));
        }
        if (!Day.isValidDay(day)) {
            throw new IllegalValueException(Day.MESSAGE_CONSTRAINTS);
        }
        final Day day = new Day(this.day);

        if (startTime == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, "startTime"));
        }

        if (endTime == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, "endTime"));
        }

        try {
            final LocalTime startScheduleTime = LocalTime.parse(this.startTime);
            final LocalTime endScheduleTime = LocalTime.parse(this.endTime);
            if (!endScheduleTime.isAfter(startScheduleTime)) {
                throw new IllegalValueException(Module.MESSAGE_CONSTRAINTS);
            }
            return new Module(moduleCode, day, startScheduleTime, endScheduleTime);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Module.MESSAGE_CONSTRAINTS);
        }
    }

}
