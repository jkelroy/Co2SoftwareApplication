package edu.nau.li_840a_interface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;
import java.text.DecimalFormat;

/**
 * Created by Andrew on 2/6/2018.
 */

public class viewScreen extends AppCompatActivity {

    String metaData;
    String[] metaDataArray;
    String metaFile;
    String graphFile;
    String graphData;
    String imageFile;
    String imageData;
    String GPSString;
    Bitmap metaImage;

    //graph variables
    private GraphView graphIds[];
    private TextView textIds[];
    private String graphArray[];

    private LineGraph co2Graph;
    private LineGraph h2oGraph;
    private LineGraph tempGraph;
    private LineGraph presGraph;

    private TextView slope;
    private TextView standardError;
    private TextView rSquared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context context = this;
        metaDataArray = new String[6];

        //set up UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_screen_test);
        TextView site = findViewById(R.id.tv_SiteName);
        TextView operator = findViewById(R.id.tv_OpName);
        TextView sampleID = findViewById(R.id.tv_SampleID);
        TextView temperature = findViewById(R.id.tv_Temp);
        TextView comments = findViewById(R.id.tv_Comments);
        TextView timeAndDate = findViewById(R.id.tv_TimeDate);
        TextView fileName = findViewById(R.id.tv_FileName);
        TextView GPS = findViewById(R.id.tv_GPS);
        TextView slope = findViewById(R.id.slope);
        TextView standardError = findViewById(R.id.standarderror);
        TextView rSquared = findViewById(R.id.rsquared);


        //graph ids array
        graphIds = new GraphView[4];
        graphIds[0] = findViewById(R.id.graph1);
        graphIds[1] = findViewById(R.id.graph2);
        graphIds[2] = findViewById(R.id.graph3);
        graphIds[3] = findViewById(R.id.graph4);

        //graph text views
        textIds = new TextView[4];
        textIds[0] = findViewById(R.id.co2display);
        textIds[1] = findViewById(R.id.h2odisplay);
        textIds[2] = findViewById(R.id.tempdisplay);
        textIds[3] = findViewById(R.id.presdisplay);

        // Image
        ImageView metaImageDisplay = findViewById(R.id.metaDataImage);

        //try to open file and save string metadata
        try {

            metaFile = getIntent().getStringExtra("FILE");
            InputStream inputStream = openFileInput(metaFile);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(inputReader);
            metaData = "";
            String line = buffReader.readLine();
            while(line != null)
            {
                metaData += line + "\n";
                line = buffReader.readLine();
            }

            graphFile = getIntent().getStringExtra("GRAPHFILE");
            inputStream = openFileInput(graphFile);
            inputReader = new InputStreamReader(inputStream);
            buffReader = new BufferedReader(inputReader);
            graphData = "";
            line = buffReader.readLine();
            while(line != null)
            {
                graphData += line + "\n";
                line = buffReader.readLine();

            }

            imageFile = getIntent().getStringExtra("IMAGE");
            inputStream = openFileInput(imageFile);
            inputReader = new InputStreamReader(inputStream);
            buffReader = new BufferedReader(inputReader);
            imageData = "";
            line = buffReader.readLine();
            while(line != null)
            {
                imageData += line;
                line = buffReader.readLine();
            }


        //return error and null for all data if it cannot open
        } catch (Exception e) {

            fileName.setText("Sorry there was an error opening your file, please try again!");
            fileName.setText(null);
            operator.setText(null);
            site.setText(null);
            sampleID.setText(null);
            temperature.setText(null);
            comments.setText(null);
            timeAndDate.setText(null);
            GPS.setText(null);

        }

        try
        {
            FileInputStream stream = openFileInput(imageFile);
            metaImageDisplay.setImageBitmap(BitmapFactory.decodeStream(stream));
        }
        catch(Exception exception)
        {

        }


        metaData = metaData.split("\n")[1];
        metaDataArray = metaData.split(",");

        //put text in each box
        fileName.setText("FILE : " + metaFile.substring(2));
        operator.setText("Operator Name : " + metaDataArray[0]);
        site.setText("Site  Name : " + metaDataArray[1]);
        sampleID.setText("Sample ID : " + metaDataArray[2]);
        temperature.setText("Temperature : " + metaDataArray[3]);
        comments.setText("Comments : " + metaDataArray[4]);
        timeAndDate.setText("Time and Date : " + metaDataArray[5]);
        GPS.setText("GPS : " + metaDataArray[6]);


        //get graph info to string array
        graphArray = new String[4];
        try
        {

            graphArray = splitGraphData(graphData);

            //format decmil to be three points
            DecimalFormat df = new DecimalFormat("#.0000");

            slope.setText("Slope : " + df.format(getSlope(graphArray[0])));
            standardError.setText("Standard Error : " + df.format(getStandardError(graphArray[0])));
            rSquared.setText("R Squared : " + df.format(getRSquared(graphArray[0])));

        }
        catch(Exception exception)
        {
            graphArray = new String[4];
            graphArray[0] = "";
            graphArray[1] = "";
            graphArray[2] = "";
            graphArray[3] = "";
        }


        // Set up graphs with data
        co2Graph = new LineGraph(graphIds[0], "CO2", "Time", "CO2",
                Color.argb(100, 0, 0, 0), graphArray[0]);
        h2oGraph = new LineGraph(graphIds[1], "H2O", "Time", "H2O",
                Color.argb(100, 0, 0, 255), graphArray[1]);
        tempGraph = new LineGraph(graphIds[2], "Temperature", "Time", "Temperature",
                Color.argb(100, 255, 0, 0), graphArray[2]);
        presGraph = new LineGraph(graphIds[3], "Pressure", "Time", "Pressure",
                Color.argb(100, 0, 255, 0), graphArray[3]);


    }

    public void showCO2(View view)
    {

        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(0, 0));


    }

    public void showH2O(View view)
    {

        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(0, 0));

    }

    public void showTemp(View view)
    {

        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(0, 0));

    }

    public void showPres(View view)
    {

        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }

    private String[] splitGraphData(String graphFileContents)
    {

        String lines[];
        String values[];
        String output[];
        String co2Points;
        String h2oPoints;
        String tempPoints;
        String presPoints;
        int count;

        co2Points = "";
        h2oPoints = "";
        tempPoints = "";
        presPoints = "";

        lines = graphFileContents.split("\n");

        for (count = 1; count < lines.length; count++)
        {

            values = lines[count].split(",");

            co2Points += values[0] + "," + values[1] + "\n";
            h2oPoints += values[0] + "," + values[2] + "\n";
            tempPoints += values[0] + "," + values[3] + "\n";
            presPoints += values[0] + "," + values[4] + "\n";

        }

        output = new String[4];

        output[0] = co2Points;
        output[1] = h2oPoints;
        output[2] = tempPoints;
        output[3] = presPoints;

        return output;

    }

    private double getStandardError(String graphPoints){
        double standardError = 0;
        float mean;
        double meanSquareRoot;
        double standardDeviation = 0;
        float sumForMean = 0;
        float sumForWhole = 0;
        String[] data;
        String[] tempData;
        int numOfPoints;

        //split data
        data = graphPoints.split("\n");
        numOfPoints = data.length;

        //loop through and get mean of all points
        for(int i = 0; i < numOfPoints; i++){
            tempData = data[i].split(",");
            sumForMean += Float.parseFloat(tempData[1]);
        }
        mean = sumForMean / numOfPoints;
        //loop through and get sum of (point - mean)^2
        for(int i = 0; i < numOfPoints; i++){
            tempData = data[i].split(",");
            sumForWhole += ((Float.parseFloat(tempData[1]) - mean) * (Float.parseFloat(tempData[1]) - mean));
        }
        //divide by n graphPoints//get square root
        standardDeviation = Math.sqrt(sumForWhole/numOfPoints);
        meanSquareRoot = Math.sqrt(numOfPoints);
        standardError = standardDeviation/meanSquareRoot;

        return standardError;
    }

    private float getSlope(String graphPoints){
        float slope = 0;
        float firstMillisecond;
        float lastMillisecond;
        float firstDataPoint;
        float lastDataPoint;
        String[] firstData;
        String[] lastData;
        String[] tempData;

        //split data
        tempData = graphPoints.split("\n");

        firstData = tempData[0].split(",");
        lastData = tempData[tempData.length - 1].split(",");

        firstMillisecond = Float.parseFloat(firstData[0]);
        lastMillisecond = Float.parseFloat(lastData[0]);
        firstMillisecond /= 1000;
        lastMillisecond /= 1000;


        firstDataPoint = Float.parseFloat(firstData[1]);
        lastDataPoint = Float.parseFloat(lastData[1]);


        slope = (lastDataPoint-firstDataPoint)/(lastMillisecond-firstMillisecond);

        return slope;
    }

    private double getRSquared(String graphPoints){

        float xTotal = 0;
        float yTotal = 0;
        float xSquaredTotal = 0;
        float ySquaredTotal = 0;
        float XY = 0;
        double rSquared = 0;
        String[] data;
        String[] tempData;
        int numOfPoints;
        float tempX;
        float tempY;

        //split data
        data = graphPoints.split("\n");
        numOfPoints = data.length;

        //loop through and get xtotal and ytotal and their squares
        for(int i = 0; i < numOfPoints; i++){
            tempData = data[i].split(",");

            tempX = Float.parseFloat(tempData[0]);
            tempY = Float.parseFloat(tempData[1]);

            //get x and y
            xTotal += tempX;
            yTotal += tempY;

            //get x and y rSquared
            xSquaredTotal += (tempX * tempX);
            ySquaredTotal += (tempY * tempY);

            //get sum of x*y
            XY += (tempX * tempY);
        }

        //rsquared equation
        rSquared = (((numOfPoints * XY) - (xTotal * yTotal)) /
                ((Math.sqrt((numOfPoints * xSquaredTotal) - (xTotal*xTotal))) * (Math.sqrt((numOfPoints * ySquaredTotal) - (yTotal*yTotal)))));

        //square r
        rSquared  = (rSquared * rSquared);

        return rSquared;
    }

}
