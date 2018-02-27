package edu.nau.li_840a_interface;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class LineGraph
{

    private GraphView graph;
    private LineGraphSeries series;
    private float yMax;
    private long xMin;
    private int numPoints;
    private static final int MAX_DATA_POINTS = 240;

    public LineGraph(GraphView id, String title, String xAxis, String yAxis, int color)
    {

        GridLabelRenderer gridLabel;

        graph = id;

        // Set the title of the graph
        graph.setTitle(title);
        graph.setTitleTextSize(48);

        // Set the x and y axis labels of the graph
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(xAxis);
        gridLabel.setVerticalAxisTitle(yAxis);

        series = new LineGraphSeries<>(new DataPoint[] {});

        series.setColor(color);
        graph.setTitleColor(color);

        graph.addSeries(series);

        yMax = 0;
        xMin = 0;
        numPoints = 0;

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

    }

    public void addPoint(float value, long time)
    {

        // Add the point to the series
        series.appendData(new DataPoint(time, value), false, MAX_DATA_POINTS);

        //
        if (value > yMax)
        {
            yMax = value;
            graph.getViewport().setMaxY(yMax * 1.25);
        }

        numPoints++;

        if (numPoints > MAX_DATA_POINTS)
        {
            graph.getViewport().setMinX(series.getLowestValueX());
        }

        graph.getViewport().setMaxX(time);

    }

}


