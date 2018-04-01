/*
 *  Author: James Beasley
 *  Last updated: March 11th, 2018
 *  Description: Uses the Android GraphView library to create a line graph. The graph is used on two
 *               different screens: the graph screen and the view screen. Graphs on the graph screen
 *               are initialized using the first constructor, which allows for data points to be
 *               added dynamically. Graphs on the view screen are initialized using the second
 *               constructor, and have their points specified by a string parameter.
 */
package edu.nau.li_840a_interface;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class LineGraph
{

    ////////////////////////////
    // Class Member Variables //
    ////////////////////////////
    private GraphView graph;
    private PointsGraphSeries series;
    private int numPoints;
    private String graphType;
    private int color;

    ///////////////
    // Constants //
    ///////////////
    private static final int MAX_DATA_POINTS = 120;

    /*
     *  Constructor for a live line graph. Allows the graph to have points added to it dynamically.
     *  To add points to the graph, use the addPoints method.
     */
    public LineGraph(GraphView id, String title, String xAxis, String yAxis, int color)
    {

        GridLabelRenderer gridLabel;

        // Assign the graph to an interface ID
        graph = id;

        // Set the title of the graph
        graph.setTitle(title);
        graph.setTitleTextSize(48);

        // Set the x and y axis labels of the graph
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(xAxis);
        gridLabel.setVerticalAxisTitle(yAxis);
        gridLabel.setLabelHorizontalHeight(10);
        gridLabel.setLabelVerticalWidth(30);

        // Initialize a new series of data points
        series = new PointsGraphSeries<>(new DataPoint[] {});

        // Assign the graph colors
        series.setColor(color);
        series.setSize(5);
        graph.setTitleColor(color);

        // Assign the new empty series to the graph
        graph.addSeries(series);

        // Initialize the number of points in the series
        numPoints = 0;

        // Set manual bounds for the graph
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        // Specify that the graph is dynamic, and can have points added to it
        graphType = "dynamic";

    }

    /*
     *  Constructor for a static line graph. Does not allow points to be added to it dynamically.
     *  Instead, takes in a file string, specifying points to add to the graph. The file string
     *  should be formatted in the following way:
     *
     *  <x-value>,<y-value>
     *  <x-value>,<y-value>
     *  <x-value>,<y-value>
     *
     *  ... and so on.
     */
    public LineGraph(GraphView id, String title, String xAxis, String yAxis, int color,
                     String fileContent)
    {

        GridLabelRenderer gridLabel;
        String[] points;
        float time;
        float value;

        // Assign the graph to an interface ID
        graph = id;

        // Set the title of the graph
        graph.setTitle(title);
        graph.setTitleTextSize(48);

        // Set the x and y axis labels of the graph
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(xAxis);
        gridLabel.setVerticalAxisTitle(yAxis);

        // Initialize a new series of data points
        series = new PointsGraphSeries<>(new DataPoint[] {});

        // Assign the graph colors
        series.setColor(color);
        series.setSize(5);
        graph.setTitleColor(color);

        // Make sure a data set was recorded before attempting to parse values
        if (!fileContent.equals(""))
        {

            // Split the file contents by line
            points = fileContent.split("\n");

            // Loop through each line
            for (String point : points)
            {

                // Parse out the milliseconds and value
                time = Float.parseFloat(point.split(",")[0]);
                value = Float.parseFloat(point.split(",")[1]);

                // Add the new data point to the series
                series.appendData(new DataPoint(time, value), false, points.length);

            }

            // After we are finished adding points to the series, add the series to the graph
            graph.addSeries(series);

            // Set the Y axis range
            graph.getViewport().setMaxY(series.getHighestValueY());
            graph.getViewport().setMinY(series.getLowestValueY());

            // Set the X axis range
            graph.getViewport().setMaxX(series.getHighestValueX());
            graph.getViewport().setMinX(series.getLowestValueX());

            // Make the graph zoomable and scalable
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setScalableY(true);

        }

        // Specify that the graph is stats, and cannot have points dynamically added to it
        graphType = "static";

        // Save the color so that it can be used in the regression line
        this.color = color;

    }

    /*
     *  Adds a point to a dynamic graph at the specified time and value. If the graph has been
     *  initialized as a static graph, then the method does not add the point.
     */
    public void addPoint(float value, float time)
    {

        // If the graph is static, and not dynamic, do not add the point
        if (graphType.equals("static"))
        {
            return;
        }

        // Scale the viewport to a little more than the highest Y value
        graph.getViewport().setMaxY(series.getHighestValueY() * 1.25);

        graph.getViewport().setMinY(series.getLowestValueY() * 0.75);

        // Add the point to the series
        series.appendData(new DataPoint(time, value), false, MAX_DATA_POINTS);

        // If the total number of points is greater than the max allowed in the series, update the
        // minimum X value to show the lowest number still stored in the series
        if (++numPoints > MAX_DATA_POINTS)
        {
            graph.getViewport().setMinX(series.getLowestValueX());
        }

        // Update the viewport to show the new maximum time
        graph.getViewport().setMaxX(time);

    }

    public void reset()
    {
        series.resetData(new DataPoint[] {});
        numPoints = 0;

    }

    public void addRegLine(double yIntercept, double slope)
    {

        double xStart;
        double xEnd;
        double yStart;
        double yEnd;
        LineGraphSeries regSeries;

        xStart = series.getLowestValueX();
        xEnd = series.getHighestValueX();

        yStart = (slope * xStart) + yIntercept;
        yEnd = (slope * xEnd) + yIntercept;

        regSeries = new LineGraphSeries<>(new DataPoint[] {});
        regSeries.setColor(color);
        regSeries.setThickness(3);

        regSeries.appendData(new DataPoint(xStart, yStart), false, MAX_DATA_POINTS);
        regSeries.appendData(new DataPoint(yStart, yEnd), false, MAX_DATA_POINTS);

        graph.addSeries(regSeries);

    }

}


