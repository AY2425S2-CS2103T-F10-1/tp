package syncsquad.teamsync.components.timetable;

import java.util.Collection;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;
import syncsquad.teamsync.model.module.Day;

/**
 * A chart based on {@link XYChart} that displays a timetable.
 *
 * Heavily inspired by @Roland's GanttChart.java, which is itself inspired by openjfx BubbleChart.java
 * https://stackoverflow.com/a/27978436
 * https://github.com/ojdkbuild/lookaside_openjfx/
 */
public class TimetableChart extends XYChart<Number, String> { // Flip axis types

    private ObservableList<PersonModulesBlock> personModulesBlocks = FXCollections.observableArrayList();

    private ObservableList<MeetingBlock> meetingBlocks = FXCollections.observableArrayList();
    private TooltipBlock tooltipBlock = new TooltipBlock();

    /** Marks the current time on today's row; hidden when today is not in the displayed week. */
    private final Line nowLine = new Line();
    private String nowDay;
    private double nowHour;

    /**
     * Constructs a {@code TimetableChart} with the specified data.
     * @param personModulesBlocks
     * @param meetingBlocks
     */
    public TimetableChart(
        ObservableList<PersonModulesBlock> personModulesBlocks,
        ObservableList<MeetingBlock> meetingBlocks) {
        super(
                new NumberAxis(0, 24, 1),
                new CategoryAxis(FXCollections.observableArrayList(Day.VALID_DAYS))
        );
        CategoryAxis yAxis = (CategoryAxis) getYAxis();
        // Dirty trick to make the gridlines timetable-like
        yAxis.heightProperty().addListener((observable, oldValue, newValue) -> {
            double displacement = -newValue.doubleValue() / (yAxis.getCategories().size() * 2);
            yAxis.translateYProperty().set(displacement);
        });
        yAxis.setTickLabelGap(10);
        yAxis.setTickMarkVisible(false);

        // Doesn't necessarily work as intended, will need further testing. Still, minor aesthetic issue
        Node chartBackground = lookup(".chart-plot-background");
        chartBackground.setStyle(
                "-fx-border-color: -color-accent-emphasis; -fx-border-width: 1 0 0 1;");

        setVerticalGridLinesVisible(false);

        // Dirty trick to invert yAxis
        FXCollections.reverse(yAxis.getCategories());
        yAxis.getCategories().add("");

        getXAxis().sideProperty().setValue(javafx.geometry.Side.TOP);
        NumberAxis xAxis = (NumberAxis) getXAxis();
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int hour = (int) Math.abs(object.doubleValue()); // Cast to integer after handling Double
                return String.format("%02d:00", hour);
            }

            @Override
            public Number fromString(String string) {
                return Double.valueOf(string.split(":")[0]); // Use Double for consistency
            }
        });

        setData(FXCollections.observableArrayList());

        loadPersonModulesBlocks(personModulesBlocks);
        loadMeetingBlocks(meetingBlocks);

        nowLine.setStroke(Color.web("#ff5555"));
        nowLine.setStrokeWidth(2);
        nowLine.setMouseTransparent(true);
        nowLine.setVisible(false);
        getPlotChildren().add(nowLine);

        yAxis.toBack();
    }

    /**
     * Marks the current time on the row of {@code day}, or hides the marker if {@code day} is null.
     *
     * @param day one of {@link Day#VALID_DAYS}, or null
     * @param hour hours since midnight
     */
    public void setNow(String day, double hour) {
        nowDay = day;
        nowHour = hour;
        nowLine.setVisible(day != null);
        requestChartLayout();
    }

    /**
     * Loads the person modules blocks into the chart.
     * @param personModulesBlocks the person modules blocks to load
     */
    public void loadPersonModulesBlocks(Collection<PersonModulesBlock> personModulesBlocks) {
        this.personModulesBlocks.setAll(personModulesBlocks);
        TooltipBlock tooltipBlock = new TooltipBlock();
        personModulesBlocks.forEach(block -> {
            block.getModuleSeries().getData().forEach(data -> {
                PersonModulesBlock.StyledModule styledModule = (PersonModulesBlock.StyledModule) data.getExtraValue();
                tooltipBlock.addEvent(
                    styledModule.getTooltipText(),
                    data.getXValue().doubleValue(),
                    data.getYValue(),
                    styledModule.getDuration()
                );
            });
        });
        this.tooltipBlock = tooltipBlock;
        loadData();
    }

    /**
     * Loads the meeting blocks into the chart.
     * @param meetingBlocks the meeting blocks to load
     */
    public void loadMeetingBlocks(Collection<MeetingBlock> meetingBlocks) {
        this.meetingBlocks.setAll(meetingBlocks);
        loadData();
    }

    private void loadData() {
        getData().clear();
        getData().addAll(personModulesBlocks.stream()
            .map(PersonModulesBlock::getModuleSeries)
            .collect(Collectors.toList()));
        getData().addAll(meetingBlocks.stream()
            .map(MeetingBlock::getMeetingSeries)
            .collect(Collectors.toList()));
        getData().addAll(tooltipBlock.getTooltipSeries());
    }

    @Override
    protected void layoutPlotChildren() {
        double blockHeight = getBlockHeight();
        NumberAxis xAxis = (NumberAxis) getXAxis();
        CategoryAxis yAxis = (CategoryAxis) getYAxis();

        personModulesBlocks.forEach(block -> block.layout(xAxis, yAxis, blockHeight));
        tooltipBlock.layout(xAxis, yAxis, blockHeight);
        meetingBlocks.forEach(block -> block.layout(xAxis, yAxis, blockHeight));
        layoutNowLine(xAxis, yAxis, blockHeight);
    }

    private void layoutNowLine(NumberAxis xAxis, CategoryAxis yAxis, double blockHeight) {
        if (nowDay == null) {
            return;
        }
        double x = xAxis.getDisplayPosition(nowHour);
        double y = yAxis.getDisplayPosition(nowDay);
        nowLine.setStartX(x);
        nowLine.setEndX(x);
        // Same vertical extent as the blocks, which are drawn centred on (y - blockHeight / 2)
        nowLine.setStartY(y - blockHeight);
        nowLine.setEndY(y);
        nowLine.toFront();
    }

    /**
     * Returns the height of a block in the timetable.
     *
     * @return The block height.
     */
    public double getBlockHeight() {
        double chartHeight = getYAxis().getHeight();
        return chartHeight / ((CategoryAxis) getYAxis()).getCategories().size() - 2;
    }

    @Override
    protected void dataItemAdded(Series<Number, String> series, int itemIndex, Data<Number, String> item) {
        Node block = createContainer(item);
        getPlotChildren().add(block);
    }

    @Override
    protected void dataItemRemoved(final Data<Number, String> item, final Series<Number, String> series) {
        final Node block = item.getNode();
        getPlotChildren().remove(block);
        removeDataItemFromDisplay(series, item);
    }

    @Override
    protected void dataItemChanged(Data<Number, String> item) {
    }

    @Override
    protected void seriesAdded(Series<Number, String> series, int seriesIndex) {
        for (int j = 0; j < series.getData().size(); j++) {
            Data<Number, String> item = series.getData().get(j);
            Node container = createContainer(item);
            getPlotChildren().add(container);
        }
    }

    @Override
    protected void seriesRemoved(final Series<Number, String> series) {
        for (XYChart.Data<Number, String> d : series.getData()) {
            final Node container = d.getNode();
            getPlotChildren().remove(container);
        }
        removeSeriesFromDisplay(series);
    }

    private Node createContainer(Data<Number, String> item) {
        Node container = item.getNode();

        if (container == null) {
            container = new StackPane();
            item.setNode(container);
        }

        return container;
    }

    @Override
    protected void updateAxisRange() {
    }
}
