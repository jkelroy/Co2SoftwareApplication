package edu.nau.li_840a_interface;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class LineGraph
{

    private GraphView graph;
    private LineGraphSeries series;
    private int numPoints;
    private long xMin;
    private long xMax;
    private float yMin;
    private float yMax;

    public LineGraph(GraphView id, String title, String xAxis, String yAxis, String color)
    {

        int graphId;

        GridLabelRenderer gridLabel;

        graph = id;

        // Set the title of the graph
        graph.setTitle(title);
        graph.setTitleTextSize(24);

        // Set the x and y axis labels of the graph
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(xAxis);
        gridLabel.setVerticalAxisTitle(yAxis);

        series = new LineGraphSeries<>(new DataPoint[] {});

        numPoints = 0;

        yMax = 1000;
        yMin = 0;
        xMax = 10;
        xMin = 0;

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(yMax);
        graph.getViewport().setMaxY(yMin);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(yMax);
        graph.getViewport().setMaxX(yMin);

    }

    public void addPoint(float value, long time)
    {

        // Add the point to the series, with a limit of 240 points
        series.appendData(new DataPoint(time, value), false, 240);

        // Add the updated series to the graph
        graph.addSeries(series);

        numPoints++;

        if (value > yMax)
        {
            yMax = value;
            graph.getViewport().setMaxY(yMax);
        }

        if (numPoints > 120)
        {
            xMax = time;
            graph.getViewport().setMaxX(xMax);
        }

    }

}
