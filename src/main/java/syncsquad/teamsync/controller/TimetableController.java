package syncsquad.teamsync.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import syncsquad.teamsync.components.timetable.MeetingBlock;
import syncsquad.teamsync.components.timetable.PersonModulesBlock;
import syncsquad.teamsync.components.timetable.TimetableChart;
import syncsquad.teamsync.model.module.Day;
import syncsquad.teamsync.viewmodel.MeetingListViewModel;
import syncsquad.teamsync.viewmodel.PersonListViewModel;

/**
 * Panel containing the timetable for a specified date.
 */
public class TimetableController extends UiPart<Region> {
    private static final String FXML = "Timetable.fxml";

    @FXML
    private VBox mainVBox;

    private TimetableChart timetable; // Day vs Hour

    /**
     * Creates a {@code Timetable} for all people and meetings, marking the current time {@code now}.
     */
    public TimetableController(PersonListViewModel personListViewModel, MeetingListViewModel meetingListViewModel,
                               ObservableValue<LocalDateTime> now) {
        super(FXML);

        timetable = new TimetableChart(
            personListViewModel.personListProperty().stream()
                .map(PersonModulesBlock::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList)),
            meetingListViewModel.meetingListProperty().stream()
                .filter(meeting -> meeting.getDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .equals(meetingListViewModel.currentWeekProperty().get()))
                .map(MeetingBlock::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );

        mainVBox.getChildren().add(timetable);
        VBox.setVgrow(timetable, javafx.scene.layout.Priority.ALWAYS);

        personListViewModel.personListProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<PersonModulesBlock> blocks = personListViewModel.personListProperty().stream()
                .map(PersonModulesBlock::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            timetable.loadPersonModulesBlocks(blocks);
        });

        meetingListViewModel.meetingListProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<MeetingBlock> blocks = meetingListViewModel.meetingListProperty().stream()
                .filter(meeting -> meeting.getDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .equals(meetingListViewModel.currentWeekProperty().get()))
                .map(MeetingBlock::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            timetable.loadMeetingBlocks(blocks);
        });

        meetingListViewModel.currentWeekProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<MeetingBlock> blocks = meetingListViewModel.meetingListProperty().stream()
                    .filter(meeting -> meeting.getDate()
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .equals(meetingListViewModel.currentWeekProperty().get()))
                    .map(MeetingBlock::new)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            timetable.loadMeetingBlocks(blocks);
            updateNow(now.getValue(), newValue);
        });

        now.addListener((observable, oldValue, newValue) ->
                updateNow(newValue, meetingListViewModel.currentWeekProperty().get()));
        updateNow(now.getValue(), meetingListViewModel.currentWeekProperty().get());
    }

    private void updateNow(LocalDateTime now, LocalDate displayedWeek) {
        boolean isNowDisplayed = now.toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .equals(displayedWeek);
        String day = isNowDisplayed ? Day.VALID_DAYS.get(now.getDayOfWeek().getValue() - 1) : null;
        timetable.setNow(day, now.toLocalTime().toSecondOfDay() / 3600.0);
    }
}
