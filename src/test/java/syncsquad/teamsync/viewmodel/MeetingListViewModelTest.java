package syncsquad.teamsync.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import syncsquad.teamsync.model.meeting.Meeting;
import syncsquad.teamsync.model.meeting.Title;

/**
 * Unit tests for {@link MeetingListViewModel}.
 */
public class MeetingListViewModelTest {
    private ObservableList<Meeting> meetingList;
    private SimpleObjectProperty<LocalDate> currentWeek;
    private MeetingListViewModel viewModel;

    @BeforeEach
    public void setUp() {
        meetingList = FXCollections.observableArrayList();
        currentWeek = new SimpleObjectProperty<>(LocalDate.now());
        viewModel = new MeetingListViewModel(meetingList, currentWeek);
    }

    @Test
    public void constructor_validList_propertyInitialized() {
        assertNotNull(viewModel.meetingListProperty(),
                "Meeting list property should be initialized");
        assertTrue(viewModel.meetingListProperty().isEmpty(),
                "Meeting list should be empty initially");
    }

    @Test
    public void meetingListProperty_listModified_reflectsChanges() {
        // Create a test meeting
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        Meeting meeting = new Meeting(date, startTime, endTime, Title.DEFAULT);

        // Add meeting to the source list
        meetingList.add(meeting);

        // Verify the property reflects the change
        assertEquals(1, viewModel.meetingListProperty().size(),
                "Meeting list property should reflect added meeting");
        assertEquals(meeting, viewModel.meetingListProperty().get(0),
                "Meeting list property should contain the added meeting");
    }

    @Test
    public void currentWeekProperty_modified_reflectsChanges() {
        // Update the current week
        LocalDate newWeek = LocalDate.now().plusDays(7);
        currentWeek.set(newWeek);

        // Verify the property reflects the change
        assertEquals(newWeek, viewModel.currentWeekProperty().get(),
                "Current week property should reflect updated week");
    }

    @Test
    public void meetingListProperty_returnsReadOnlyProperty() {
        // Verify that the property is read-only
        assertTrue(viewModel.meetingListProperty() instanceof javafx.beans.property.ReadOnlyListProperty,
                "Meeting list property should be read-only");
    }

    @Test
    public void currentWeekProperty_returnsReadOnlyProperty() {
        // Verify that the property is read-only
        assertTrue(viewModel.currentWeekProperty() instanceof javafx.beans.property.ReadOnlyObjectProperty,
                "Current week property should be read-only");
    }
}
