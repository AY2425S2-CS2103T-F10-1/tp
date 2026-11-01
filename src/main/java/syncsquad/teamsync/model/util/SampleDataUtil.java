package syncsquad.teamsync.model.util;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import syncsquad.teamsync.logic.parser.ParserUtil;
import syncsquad.teamsync.logic.parser.exceptions.ParseException;
import syncsquad.teamsync.model.AddressBook;
import syncsquad.teamsync.model.ReadOnlyAddressBook;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.meeting.Title;
import syncsquad.teamsync.model.module.Day;
import syncsquad.teamsync.model.module.Module;
import syncsquad.teamsync.model.module.ModuleCode;
import syncsquad.teamsync.model.person.Address;
import syncsquad.teamsync.model.person.Email;
import syncsquad.teamsync.model.person.Name;
import syncsquad.teamsync.model.person.Person;
import syncsquad.teamsync.model.person.Phone;
import syncsquad.teamsync.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("e0507399@u.nus.edu"),
                new Address("Eusoff Hall"),
                getTagSet("Leader"), getModuleSet("CS2103T FRI 16:00 18:00", "CS2101 THU 12:00 15:00")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("e1234567@u.nus.edu"),
                new Address("RC4"),
                getTagSet("Frontend", "UI"), getModuleSet("ACC1701 MON 12:00 14:00")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@gmail.com"),
                new Address("UTR"),
                getTagSet("Devops"), getModuleSet("ES2660 TUE 12:00 15:00", "IS1108 MON 14:00 16:00")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("li_david@u.nus.edu"),
                new Address("PGPR Light House"),
                getTagSet("Backend"), getModuleSet("GESS1035 TUE 18:00 20:00")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("e9876543@u.nus.edu"),
                new Address("Eusoff Hall"),
                getTagSet("TopDeveloper"), getModuleSet("MA1301 WED 15:00 15:30", "CDE2605 THU 17:00 19:00")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@yahoo.com"),
                new Address("RC4"),
                getTagSet("Slacker"), getModuleSet("BN2001 FRI 08:30 11:30", "BT1101 THU 16:00 18:00"))
        };
    }

    public static Meeting[] getSampleMeetings() throws ParseException {
        return new Meeting[]{
            new Meeting(ParserUtil.parseDate("01-01"), ParserUtil.parseTime("14:00"), ParserUtil.parseTime("16:00"),
                new Title("Project kickoff")),
            new Meeting(ParserUtil.parseDate("03-03"), ParserUtil.parseTime("19:45"), ParserUtil.parseTime("20:50"),
                new Title("User guide review")),
            new Meeting(ParserUtil.parseDate("05-04"), ParserUtil.parseTime("10:00"), ParserUtil.parseTime("15:00"),
                new Title("Sprint planning")),
            new Meeting(ParserUtil.parseDate("12-04"), ParserUtil.parseTime("8:00"), ParserUtil.parseTime("14:00"),
                new Title("Demo rehearsal")),
            new Meeting(ParserUtil.parseDate("08-08"), ParserUtil.parseTime("14:00"), ParserUtil.parseTime("16:00"),
                new Title("Bug bash")),
            new Meeting(ParserUtil.parseDate("12-12"), ParserUtil.parseTime("12:00"), ParserUtil.parseTime("13:00"),
                new Title("Final submission check"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() throws ParseException {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Meeting sampleMeeting: getSampleMeetings()) {
            sampleAb.addMeeting(sampleMeeting);
        }
        return sampleAb;
    }

    /**
     * Returns a module set containing the list of strings given.
     */
    public static Set<Module> getModuleSet(String... strings) {
        return Arrays.stream(strings)
                .map((s) -> {
                    String[] params = s.split(" ");
                    ModuleCode moduleCode = new ModuleCode(params[0]);
                    Day day = new Day(params[1]);
                    LocalTime startScheduleTime = LocalTime.parse(params[2]);
                    LocalTime endScheduleTime = LocalTime.parse(params[3]);
                    return new Module(moduleCode, day, startScheduleTime, endScheduleTime);
                })
                .collect(Collectors.toSet());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
