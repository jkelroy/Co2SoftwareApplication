package edu.nau.li_840a_interface;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import java.util.ArrayList;
import java.util.Date;

/*
 *
 */
public class GraphManager implements Runnable
{

    ////////////////////////////
    // CLASS MEMBER VARIABLES //
    ////////////////////////////
    private ArrayList<DataSeries> dataArray;
    private Activity activity;
    private LineGraph co2Graph;
    private LineGraph h2oGraph;
    private LineGraph tempGraph;
    private LineGraph presGraph;
    private TextView co2Display;
    private TextView h2oDisplay;
    private TextView tempDisplay;
    private TextView presDisplay;
    private long startTime;
    private boolean running;
    private boolean logging;
    private String lastData;

    ///////////////
    // CONSTANTS //
    ///////////////
    private static final int SLEEP_TIME = 500;


    /*
     *  Constructor for the GraphManager. Finds the start time, initializes the graphs, and sets
     *  other class member variables.
     */
    public GraphManager(Activity activity, GraphView[] graphIds, TextView textIds[])
    {

        Date time;

        this.activity = activity;

        // Initialize our array of readings
        dataArray = new ArrayList<DataSeries>();

        // Initialize all of the graphs
        co2Graph = new LineGraph(graphIds[0], "CO2 Readings", "Time", "CO2",
                Color.argb(100, 0, 0, 0));
        h2oGraph = new LineGraph(graphIds[1], "H2O Readings", "Time", "H2O",
                Color.argb(100, 0, 0, 255));
        tempGraph = new LineGraph(graphIds[2], "Temperature Readings", "Time", "Temperature",
                Color.argb(100, 255, 0, 0));
        presGraph = new LineGraph(graphIds[3], "Pressure Readings", "Time", "Pressure",
                Color.argb(100, 0, 255, 0));

        // Get the IDs for the text views used to display the data values
        co2Display = textIds[0];
        h2oDisplay = textIds[1];
        tempDisplay = textIds[2];
        presDisplay = textIds[3];

        // Get the start time
        time = new Date();
        startTime = time.getTime();

        // Set the graph in motion
        running = true;

        // Assume we are not logging a subset at the start
        logging = false;

    }

    /*
     *  Implementation of the runnable interface. Reads in data from the instrument, calculated the
     *  time that data came in, and adds the data to the graphs.
     */
    public void run()
    {

        String data;
        Date time;
        long currentTime;
        long timeDiff;

        // Loops until the screen is deconstructed
        while (running)
        {

            // Get the latest data from the instrument
            data = this.getData();

            // Get the current time
            time = new Date();
            currentTime = time.getTime();

            // Calculate the time difference between when the screen started
            timeDiff = currentTime - startTime;

            // Add the points to the graphs
            this.addPoints(data, timeDiff);

            try
            {
                // Wait half a second
                Thread.sleep(SLEEP_TIME);

            }
            catch (Exception exception)
            {
                System.out.println("CANT SLEEP");
            }

        }

    }

    public void updateData(String data)
    {
        lastData = data;
    }

    //////////////////////
    // Button Functions //
    //////////////////////

    /*
     *  Runs when the "Finalize" button is clicked.
     */
    public void deconstruct()
    {

        running = false;

        // TODO: Create new folder and save files inside of it

    }

    /*
     *  Runs when the "Start Log" button is clicked.
     */
    public void startlogging()
    {

        // Clear the log of any previously saved data points
        dataArray = new ArrayList<DataSeries>();

        // Signal that we should be adding points to the log
        logging = true;

    }

    /*
     *  Runs when the "Stop Log" button is clicked.
     */
    public void stoplogging()
    {

        // Signal that we shouldn't be adding points to the log
        logging = false;
    }

    /*
     *  Allows the graph manager to be printed. This is used for storing the
     *  data of all graphs in a text file.
     */
    public String toString()
    {

        String output;

        // Initialize the output string
        output = "";

        // Loop through each data series in the data array
        for (DataSeries series : dataArray)
        {

            // Append the series string and add a line break
            output += series.toString() + "\n";

        }

        // Return the final output string
        return output;

    }

    ///////////////////////
    // Private Functions //
    ///////////////////////

    /*
     *
     */
    private String getData()
    {
        return lastData;
    }

    /*
     *  Takes in a string of data from the instrument, converts it into a
     *  DataSeries, adds that DataSeries to the log, and updates each
     *  graph using the new information.
     */
    private void addPoints(String data, long time)
    {

        final DataSeries newSeries;

        // Initialize the new data series
        newSeries = new DataSeries(data, time);

        // Add the new data series to the array of all data series
        if (logging)
        {
            dataArray.add(newSeries);
        }

        // Update each graph with the data series
        co2Graph.addPoint(newSeries.co2, newSeries.time);
        h2oGraph.addPoint(newSeries.h2o, newSeries.time);
        tempGraph.addPoint(newSeries.temp, newSeries.time);
        presGraph.addPoint(newSeries.pres, newSeries.time);

        // These updates must be run on the UI thread in order to work
        activity.runOnUiThread(new Runnable() {
            public void run() {

                // Update each text view with the data series
                co2Display.setText(String.valueOf(newSeries.co2));
                h2oDisplay.setText(String.valueOf(newSeries.h2o));
                tempDisplay.setText(String.valueOf(newSeries.temp));
                presDisplay.setText(String.valueOf(newSeries.pres));

            }
        });

    }

    /////////////////////
    // Private Classes //
    /////////////////////

    /*
     *  Basic object used to store and access values parsed from the data.
     */
    private class DataSeries
    {

        public long time;
        public float co2;
        public float h2o;
        public float temp;
        public float pres;

        /*
         *  Constructor for the DataSeries. Takes in the data from the
         *  instrument, parses out the four relevant values, and assigns them
         *  to their corresponding variables. Also assigns the time of the data
         *  points based off the time parameter.
         */
        public DataSeries(String data, long time)
        {

            float[] parse;

            this.time = time;

            // TODO: Parse data string for values and assign the variables
            parse = parseData(data);

            co2 = parse[0];
            h2o = parse[1];
            temp = parse[2];
            pres = parse[3];

        }

        private float[] parseData(String data)
        {

            float[] output;

            System.out.println("DATA: " + data);

            output = new float[4];

            try
            {
                output[0] = stringToFloat(data.split("<co2>")[1].split("</co2>")[0]);
            }
            catch(Exception exception)
            {
                output[0] = (float) 0.0;
            }

            try
            {
                output[1] = Float.parseFloat(data.split("<h2o>")[1].split("</h2o>")[0]);
            }
            catch(Exception exception)
            {
                output[1] = (float) 0.0;
            }

            try
            {
                output[2] = stringToFloat(data.split("<celltemp>")[1].split("</celltemp>")[0]);
            }
            catch(Exception exception)
            {
                output[2] = (float) 0.0;
            }

            try
            {
                output[3] = stringToFloat(data.split("<cellpres>")[1].split("</cellpres>")[0]);
            }
            catch(Exception exception)
            {
                output[3] = (float) 0.0;
            }

            return output;

        }

        private float stringToFloat(String input)
        {

            int count;
            float number;
            int exponent;

            number = Float.parseFloat(input.split("e")[0]);
            exponent = Integer.parseInt(input.split("e")[1]);

            for (count = 0; count < exponent; count++)
            {
                number *= 10;
            }

            return number;


        }

        /*
         *  Allows the series to be printed as string. Used for logging to a
         *  text file.
         */
        public String toString()
        {

            String output;

            // Initialize our output string
            output = "";

            // Build the string using our values
            output += "Milliseconds: " + time + " (";
            output += "CO2: " + co2 + ", ";
            output += "H2O: " + h2o + ", ";
            output += "Temp: " + temp + ", ";
            output += "Pressure: " + pres;
            output += ")";

            // Return the output
            return output;

        }

    }

}
