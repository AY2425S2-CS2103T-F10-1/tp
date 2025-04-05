package syncsquad.teamsync.components.timetable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A block that displays a tooltip for each event in the timetable.
 * Overlapping events are merged into a single tooltip.
 */
public class TooltipBlock implements TimetableDisplayable {
    private final Map<String, List<TooltipEvent>> dayEvents;
    private final Map<String, XYChart.Series<Number, String>> tooltipSeriesMap;

    /**
     * Constructs a new TooltipBlock.
     */
    public TooltipBlock() {
        this.dayEvents = new HashMap<>();
        this.tooltipSeriesMap = new HashMap<>();
    }

    /**
     * Returns the tooltip series for the timetable.
     *
     * @return A collection of tooltip series.
     */
    public Collection<XYChart.Series<Number, String>> getTooltipSeries() {
        return this.tooltipSeriesMap.values();
    }

    /**
     * Adds an event to the timetable.
     * Overlapping events are merged into a single tooltip.
     *
     * @param eventName The name of the event.
     * @param startTime The start time of the event.
     * @param day The day of the event.
     * @param duration The duration of the event.
     */
    public void addEvent(String eventName, double startTime, String day, double duration) {
        TooltipEvent event = new TooltipEvent(eventName, startTime, startTime + duration);

        if (this.dayEvents.containsKey(day)) {
            appendEvent(day, event, 0);

            XYChart.Series<Number, String> series = this.tooltipSeriesMap.get(day);
            series.getData().setAll(this.dayEvents.get(day).stream()
                    .map(e -> new XYChart.Data<Number, String>(e.getStartTime(), day, e))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        } else {
            List<TooltipEvent> events = new ArrayList<>();
            events.add(event);
            this.dayEvents.put(day, events);

            ObservableList<XYChart.Data<Number, String>> data = FXCollections.observableArrayList();
            data.add(new XYChart.Data<>(event.getStartTime(), day, event));
            this.tooltipSeriesMap.put(day, new XYChart.Series<>(data));
        }
    }

    @Override
    public void layout(NumberAxis hourAxis, CategoryAxis dayAxis, double blockHeight) {
        for (String day : this.dayEvents.keySet()) {
            XYChart.Series<Number, String> series = this.tooltipSeriesMap.get(day);
            for (XYChart.Data<Number, String> item : series.getData()) {
                double x = hourAxis.getDisplayPosition(item.getXValue());
                double y = dayAxis.getDisplayPosition(item.getYValue());
                if (Double.isNaN(x) || Double.isNaN(y)) {
                    continue;
                }
                Node block = item.getNode();
                if (block == null || !(block instanceof StackPane)) {
                    continue;
                }

                assert item.getExtraValue() instanceof TooltipEvent;
                TooltipEvent tooltipEvent = (TooltipEvent) item.getExtraValue();
                StackPane region = (StackPane) block;

                Rectangle rectangle;

                if (region.getShape() == null) {
                    rectangle = new Rectangle(blockHeight,
                            tooltipEvent.getDuration()
                            * Math.abs(hourAxis.getScale()));
                    region.getChildren().add(rectangle);
                } else if (region.getShape() instanceof Rectangle existingRectangle) {
                    rectangle = existingRectangle;
                } else {
                    return;
                }
                rectangle.setWidth(tooltipEvent.getDuration()
                    * Math.abs(hourAxis.getScale()));
                rectangle.setHeight(blockHeight);

                // This puts the rectangle in the middle of the gridlines
                x += rectangle.getWidth() / 2.0 + 1;
                y -= rectangle.getHeight() / 2.0;

                rectangle.setFill(Color.TRANSPARENT);

                Tooltip tooltip = new Tooltip(tooltipEvent.description);
                Tooltip.install(region, tooltip);

                // Note: workaround for RT-7689 - saw this in ProgressControlSkin
                // The region doesn't update itself when the shape is mutated in place, so we
                // null out and then restore the shape in order to force invalidation.
                region.setShape(null);
                region.setShape(rectangle);
                region.setScaleShape(false);
                region.setCenterShape(false);
                region.setCacheShape(false);

                region.setLayoutX(x);
                region.setLayoutY(y);
                region.toFront();
            }
        }
    }


    /**
     * Represents an event with a tooltip in the timetable.
     */
    private final class TooltipEvent {
        private String description;
        private double startTime;
        private double endTime;

        /**
         * Constructs a {@code TooltipEvent}.
         *
         * @param description The description of the event.
         * @param startTime The start time of the event.
         * @param endTime The end time of the event.
         */
        public TooltipEvent(String description, double startTime, double endTime) {
            this.description = description;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public double getStartTime() {
            return this.startTime;
        }

        public double getDuration() {
            return this.endTime - this.startTime;
        }

        @Override
        public String toString() {
            return this.description.split("\n").length + " " + this.startTime + " " + this.endTime;
        }
    }

    private void appendEvent(String day, TooltipEvent event, int insertIndex) {
        if (event.startTime == event.endTime) {
            return;
        }

        List<TooltipEvent> events = this.dayEvents.get(day);
        for (int i = insertIndex; i < events.size(); i++) {
            TooltipEvent e = events.get(i);
            if (e.endTime <= event.startTime) {
                insertIndex++;
            }
        }

        if (insertIndex >= events.size()) {
            // N N E E
            events.add(event);
            return;
        }

        TooltipEvent nextEvent = events.get(insertIndex);

        if (event.endTime <= nextEvent.startTime) {
            // E E N N
            events.add(insertIndex, event);
            return;
        }

        if (event.startTime <= nextEvent.startTime && nextEvent.endTime <= event.endTime) {
            // E N N E
            TooltipEvent newEvent = new TooltipEvent(
                event.description,
                event.startTime,
                nextEvent.startTime);
            nextEvent.description = nextEvent.description + "\n" + event.description;
            event.startTime = newEvent.endTime;
            if (newEvent.startTime != newEvent.endTime) {
                events.add(insertIndex, newEvent);
            }
            appendEvent(day, event, insertIndex + 1);
            return;
        }

        if (event.startTime <= nextEvent.startTime && event.endTime <= nextEvent.endTime) {
            // E N E N
            TooltipEvent newEvent = new TooltipEvent(
                event.description,
                event.startTime,
                nextEvent.startTime);
            TooltipEvent remEvent = new TooltipEvent(
                event.description,
                event.endTime,
                nextEvent.endTime);
            nextEvent.description = nextEvent.description + "\n" + event.description;
            nextEvent.endTime = event.endTime;
            if (newEvent.startTime != newEvent.endTime) {
                events.add(insertIndex + 1, newEvent);
            }
            appendEvent(day, remEvent, insertIndex + 1);
            return;
        }

        if (nextEvent.startTime <= event.startTime && event.endTime <= nextEvent.endTime) {
            // N E E N
            TooltipEvent newEvent = new TooltipEvent(
                nextEvent.description,
                nextEvent.startTime,
                event.endTime);
            TooltipEvent remEvent = new TooltipEvent(
                nextEvent.description,
                event.endTime,
                nextEvent.endTime);
            nextEvent.description = nextEvent.description + "\n" + event.description;
            nextEvent.startTime = newEvent.endTime;
            nextEvent.endTime = remEvent.startTime;

            if (newEvent.startTime != newEvent.endTime) {
                events.add(insertIndex, newEvent);
            }
            appendEvent(day, event, insertIndex + 1);
            return;
        }

        if (nextEvent.startTime <= event.startTime && nextEvent.endTime <= event.endTime) {
            // N E N E
            TooltipEvent newEvent = new TooltipEvent(
                nextEvent.description,
                nextEvent.startTime,
                event.startTime);
            TooltipEvent remEvent = new TooltipEvent(
                event.description,
                nextEvent.endTime,
                event.endTime);
            nextEvent.description = nextEvent.description + "\n" + event.description;
            nextEvent.startTime = newEvent.endTime;
            nextEvent.endTime = remEvent.startTime;

            if (newEvent.startTime != newEvent.endTime) {
                events.add(insertIndex, newEvent);
            }
            appendEvent(day, remEvent, insertIndex + 1);
            return;
        }
    }
}
