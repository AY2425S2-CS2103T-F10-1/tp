package syncsquad.teamsync.controller;

import java.time.LocalDateTime;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
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

    //TODO: not implemented for meetings
    private LocalDateTime date;
    private TimetableChart<String, Number> timetable;

    /**
     * Creates a {@code Timetable} for all people and meetings.
     */
    public TimetableController(PersonListViewModel personListViewModel, MeetingListViewModel meetingListViewModel) {
        super(FXML);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.sideProperty().setValue(javafx.geometry.Side.TOP);
        xAxis.setCategories(FXCollections.observableArrayList(Day.VALID_DAYS));

        // Dirty trick to invert axis: set them to negative values, then get the Formatter to
        // strip the negative sign.
        NumberAxis yAxis = new NumberAxis(-24, 0, 1); // Time axis from 0000 to 2400
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int hour = Math.abs(object.intValue());
                return String.format("%02d:00", hour);
            }

            @Override
            public Number fromString(String string) {
                return Integer.valueOf(string.split(":")[0]);
            }
        });

        timetable = new TimetableChart<>(xAxis, yAxis);

        mainVBox.getChildren().add(timetable);
        VBox.setVgrow(timetable, javafx.scene.layout.Priority.ALWAYS);

        personListViewModel.personListProperty().forEach(person -> {
            XYChart.Series<String, Number> personSeries = new XYChart.Series<>();

            person.getModules().forEach(module -> {
                String day = module.getDay().toString();
                double startTime = module.getStartTime().toSecondOfDay() / 3600.0;
                double endTime = module.getEndTime().toSecondOfDay() / 3600.0;
                double duration = endTime - startTime;
                personSeries.getData().add(new XYChart.Data<>(day, -startTime,
                        new TimetableChart.ExtraData(duration, FXCollections.observableArrayList(Styles.ACCENT),
                                module.getModuleCode().toString(), person.getName().toString())));
            });

            timetable.getData().add(personSeries);
        });

        personListViewModel.personListProperty().addListener((observable, oldValue, newValue) -> {
            timetable.getData().clear();
            newValue.forEach(person -> {
                XYChart.Series<String, Number> personSeries = new XYChart.Series<>();

                person.getModules().forEach(module -> {
                    String day = module.getDay().toString();
                    double startTime = module.getStartTime().toSecondOfDay() / 3600.0;
                    double endTime = module.getEndTime().toSecondOfDay() / 3600.0;
                    double duration = endTime - startTime;
                    personSeries.getData().add(new XYChart.Data<>(day, -startTime,
                            new TimetableChart.ExtraData(duration, FXCollections.observableArrayList(Styles.ACCENT),
                                    module.getModuleCode().toString(), person.getName().toString())));
                });

                timetable.getData().add(personSeries);
            });
        });
    }
}
