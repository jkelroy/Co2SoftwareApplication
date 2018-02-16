package edu.nau.li_840a_interface;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/*
 *
 */
public class GraphManager implements Runnable
{

    private ArrayList<DataSeries> dataArray;
    private LineGraph co2Graph;
    private LineGraph h2oGraph;
    private LineGraph tempGraph;
    private LineGraph presGraph;
    private long startTime;
    private boolean running;
    private boolean logging;

    public GraphManager(GraphView[] graphIds)
    {

        Date time;

        // Initialize our array of readings
        dataArray = new ArrayList<DataSeries>();

        // Initialize all of the graphs
        co2Graph = new LineGraph(graphIds[0], "CO2 Readings", "Time", "CO2", "Green");
        h2oGraph = new LineGraph(graphIds[1], "H2O Readings", "Time", "H2O", "Blue");
        tempGraph = new LineGraph(graphIds[2], "Temperature Readings", "Time", "Temperature", "Red");
        presGraph = new LineGraph(graphIds[3], "Pressure Readings", "Time", "Pressure", "Yellow");

        // Get the start time
        time = new Date();
        startTime = time.getTime();

        // Set the graph in motion
        running = true;

        // Assume we are not logging a subset at the start
        logging = false;

    }

    /*
     *
     */
    public void run()
    {
        String data;
        Date time;
        long currentTime;
        long timeDiff;

        //TESTING VARS
        long x;
        float y;
        x = 0;

        while (running)
        {

            try
            {

                // Wait half a second
                Thread.sleep(500);

                // Get the latest data from the instrument
                //data = this.getData();

                // Get the current time
                //time = new Date();
                //currentTime = time.getTime();

                // Calculate the time difference between when the screen started
                //timeDiff = currentTime - startTime;

                // Add the points to the graphs
                //this.addPoints(data, timeDiff);
                x += 100;
                y = (float) 100.0;
                co2Graph.addPoint(y, x);

            }
            catch (Exception exception)
            {

            }



        }

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
        logging = true;
    }

    /*
     *  Runs when the "Stop Log" button is clicked.
     */
    public void stoplogging()
    {
        logging = false;
    }

    /*
     *  ALlows the graph manager to be printed. This is used for storing the
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



        return null;
    }

    /*
     *  Takes in a string of data from the instrument, converts it into a
     *  DataSeries, adds that DataSeries to the master array, and updates each
     *  graph using the new information.
     */
    private void addPoints(String data, long time)
    {

        DataSeries newSeries;

        // Initialize the new data series
        newSeries = new DataSeries(data, time);

        // Add the new data series to the array of all data series
        dataArray.add(newSeries);

        // // Update each graph with the data series
        co2Graph.addPoint(newSeries.co2, newSeries.time);
        h2oGraph.addPoint(newSeries.h2o, newSeries.time);
        tempGraph.addPoint(newSeries.temp, newSeries.time);
        presGraph.addPoint(newSeries.pres, newSeries.time);

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
         *  instrument, parses out the four relevent values, and assigns them
         *  to their corresponding variables. Also assigns the time of the data
         *  points based off the time parameter.
         */
        public DataSeries(String data, long time)
        {

            this.time = time;

            // TODO: Parse data string for values and assign the variables

            Random random = new Random();

            co2 = random.nextFloat() * 1000;
            h2o = random.nextFloat() * 1000;
            temp = random.nextFloat() * 1000;
            pres = random.nextFloat() * 1000;

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
