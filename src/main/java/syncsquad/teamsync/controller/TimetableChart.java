package syncsquad.teamsync.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A chart based on {@link XYChart} that displays a timetable.
 *
 * Heavily inspired by @Roland's GanttChart.java, which is itself inspired by openjfx BubbleChart.java
 * https://stackoverflow.com/a/27978436
 * https://github.com/ojdkbuild/lookaside_openjfx/
 */
public class TimetableChart<X, Y> extends XYChart<X, Y> {

    private static final Color[] COLORS = {
        Color.rgb(255, 0, 0, 0.3), Color.rgb(0, 255, 0, 0.3), Color.rgb(0, 0, 255, 0.3),
        Color.rgb(255, 255, 0, 0.3), Color.rgb(0, 255, 255, 0.3), Color.rgb(255, 0, 255, 0.3),
        Color.rgb(128, 0, 0, 0.3), Color.rgb(0, 128, 0, 0.3), Color.rgb(0, 0, 128, 0.3),
        Color.rgb(128, 128, 0, 0.3), Color.rgb(0, 128, 128, 0.3), Color.rgb(128, 0, 128, 0.3),
        Color.rgb(192, 192, 192, 0.3), Color.rgb(128, 128, 128, 0.3), Color.rgb(64, 64, 64, 0.3),
        Color.rgb(255, 128, 0, 0.3), Color.rgb(128, 255, 0, 0.3), Color.rgb(0, 255, 128, 0.3),
        Color.rgb(0, 128, 255, 0.3), Color.rgb(128, 0, 255, 0.3), Color.rgb(255, 0, 128, 0.3),
        Color.rgb(255, 64, 64, 0.3), Color.rgb(64, 255, 64, 0.3), Color.rgb(64, 64, 255, 0.3),
        Color.rgb(255, 255, 128, 0.3), Color.rgb(128, 255, 255, 0.3), Color.rgb(255, 128, 255, 0.3),
        Color.rgb(192, 64, 64, 0.3), Color.rgb(64, 192, 64, 0.3), Color.rgb(64, 64, 192, 0.3)
    };

    /**
     * {@code XYChart.Data} supports extra values that can be plotted in any way the chart needs.
     * The {@code TimetableChart} uses this to store the length of the block and the style class.
     */
    protected static class ExtraData {
        private double length;
        private ObservableList<String> styleClass;
        private String moduleCode;
        private String personName;

        public ExtraData(double length, ObservableList<String> styleClass, String moduleCode, String personName) {
            super();
            this.length = length;
            this.styleClass = FXCollections.observableArrayList(styleClass);
            this.moduleCode = moduleCode;
            this.personName = personName;
        }

        public double getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public ObservableList<String> getStyleClass() {
            return styleClass;
        }

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleName) {
            this.moduleCode = moduleName;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        @Override
        public String toString() {
            return "ExtraData [length=" + length + ", styleClass=" + styleClass
                    + ", moduleName=" + moduleCode + ", personName=" + personName + "]";
        }
    }

    public TimetableChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.<Series<X, Y>>observableArrayList());
    }

    /**
     * Constructs a {@code TimetableChart} with the specified axes and data.
     * @param xAxis
     * @param yAxis
     * @param data
     */
    public TimetableChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis,
                          @NamedArg("data") ObservableList<Series<X, Y>> data) {
        super(xAxis, yAxis);
        assert xAxis instanceof CategoryAxis : "TimetableChart only supports CategoryAxis for xAxis";
        assert yAxis instanceof NumberAxis : "TimetableChart only supports NumberAxis for yAxis";
        setData(data);
    }

    private static ObservableList<String> getStyleClass(Object obj) {
        assert obj instanceof ExtraData;
        return ((ExtraData) obj).getStyleClass();
    }

    private static double getLength(Object obj) {
        assert obj instanceof ExtraData;
        return ((ExtraData) obj).getLength();
    }

    @Override
    protected void layoutPlotChildren() {
        double blockWidth = getBlockWidth();

        for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
            Series<X, Y> series = getData().get(seriesIndex);
            Iterator<Data<X, Y>> iter = getDisplayedDataIterator(series);
            while (iter.hasNext()) {
                Data<X, Y> item = iter.next();
                double x = getXAxis().getDisplayPosition(item.getXValue());
                double y = getYAxis().getDisplayPosition(item.getYValue());
                if (Double.isNaN(x) || Double.isNaN(y)) {
                    continue;
                }
                Node block = item.getNode();
                Rectangle rectangle;
                if (block != null) {
                    if (block instanceof StackPane) {
                        StackPane region = (StackPane) item.getNode();
                        if (region.getShape() == null) {
                            rectangle = new Rectangle(blockWidth,
                                    getLength(item.getExtraValue())
                                    * Math.abs(((NumberAxis) getYAxis()).getScale()));
                            region.getChildren().add(rectangle);
                        } else if (region.getShape() instanceof Rectangle existingRectangle) {
                            rectangle = existingRectangle;
                        } else {
                            return;
                        }
                        rectangle.setHeight(getLength(item.getExtraValue())
                                * Math.abs(((NumberAxis) getYAxis()).getScale()));
                        rectangle.setWidth(blockWidth);
                        rectangle.setArcWidth(10);
                        rectangle.setArcHeight(10);

                        // The second part of the dirty trick to invert yAxis
                        y += rectangle.getHeight() / 2.0;

                        rectangle.getStyleClass().addAll(getStyleClass(item.getExtraValue()));
                        rectangle.setFill(COLORS[seriesIndex % COLORS.length]);
                        rectangle.toFront();

                        ExtraData extraData = (ExtraData) item.getExtraValue();
                        String tooltipText = extraData.getModuleCode() + '\n' + extraData.getPersonName();
                        Tooltip tooltip = new Tooltip(tooltipText);
                        Tooltip.install(region, tooltip);

                        // Note: workaround for RT-7689 - saw this in ProgressControlSkin
                        // The region doesn't update itself when the shape is mutated in place, so we
                        // null out and then restore the shape in order to force invalidation.
                        region.setShape(null);
                        region.setShape(rectangle);
                        region.setScaleShape(false);
                        region.setCenterShape(false);
                        region.setCacheShape(false);

                        block.setLayoutX(x);
                        block.setLayoutY(y);
                    }
                }
            }
        }
    }

    public double getBlockWidth() {
        double chartWidth = getXAxis().getWidth();
        return chartWidth / ((CategoryAxis) getXAxis()).getCategories().size();
    }

    @Override
    protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {
        Node block = createContainer(item);
        getPlotChildren().add(block);
    }

    @Override
    protected void dataItemRemoved(final Data<X, Y> item, final Series<X, Y> series) {
        final Node block = item.getNode();
        getPlotChildren().remove(block);
        removeDataItemFromDisplay(series, item);
    }

    @Override
    protected void dataItemChanged(Data<X, Y> item) {
    }

    @Override
    protected void seriesAdded(Series<X, Y> series, int seriesIndex) {
        for (int j = 0; j < series.getData().size(); j++) {
            Data<X, Y> item = series.getData().get(j);
            Node container = createContainer(item);
            getPlotChildren().add(container);
        }
    }

    @Override
    protected void seriesRemoved(final Series<X, Y> series) {
        for (XYChart.Data<X, Y> d : series.getData()) {
            final Node container = d.getNode();
            getPlotChildren().remove(container);
        }
        removeSeriesFromDisplay(series);
    }

    private Node createContainer(Data<X, Y> item) {
        Node container = item.getNode();

        if (container == null) {
            container = new StackPane();
            item.setNode(container);
        }

        container.getStyleClass().addAll(getStyleClass(item.getExtraValue()));

        return container;
    }

    @Override
    protected void updateAxisRange() {
        final Axis<Y> ya = getYAxis();
        List<Y> yData = null;

        if (ya.isAutoRanging()) {
            yData = new ArrayList<>();
        }

        if (yData != null) {
            for (Series<X, Y> series : getData()) {
                for (Data<X, Y> data : series.getData()) {
                    yData.add(data.getYValue());
                }
            }
            ya.invalidateRange(yData);
        }
    }
}
