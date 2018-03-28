package edu.nau.li_840a_interface;

//we will want the strings to be empty strings passed in not gone
//have all strings be there
//TODO: error check for no strings there is a better way for checking if no input entered

//package com.example.joey.test;
//import java.awt.image;
//import java.lang.Object;
//import java.awt.image.BufferedImage;
import java.lang.Integer;
import java.lang.Float;

public class DataFolder
{
    //given data
    public String data;
    public String metaData;
    //public static BufferedImage image;
    //saved inputs split
    public static String siteName;
    public static String time;
    public static String GPSCoordinants;
    public static String plotNumber;
    public static String operatorName;
    public static String temperature;
    public static String comments;
    //array of metaData length
    public static int arrayLength;
    public int arrayCounter = 0;
    //temp holders of information
    public String tempString;
    public String tempData;
    //define constant strings
    public static final String SITE_NAME = "Site Name:";
    public static final String TIME = "Time:";
    public static final String GPS = "GPS:";
    public static final String PLOT_NUMBER = "Plot Number:";
    public static final String OPERATOR_NAME = "Operator Name:";
    public static final String TEMPERATURE = "Temperature:";
    public static final String COMMENTS = "Comments:";
    public static final String COLON = ":";

    public DataFolder(String data, String metaData)
    {
        this.data = data;
        this.metaData = metaData;
        //this.image = image;

        //split string into an array
        String metaDataArray[] = metaData.split("\\r?\\n");
        arrayLength = metaDataArray.length;
        splitData(metaDataArray);

    }

    public void splitData(String metaDataArray[])
    {
        while(arrayCounter < arrayLength)
        {
            tempString = metaDataArray[arrayCounter];
            tempData = "";

            if (tempString.contains(SITE_NAME))
            {
                //1 vs 2 depends on if there is a space or not in the string
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                siteName = tempData;
            }
            else if (tempString.contains(TIME))
            {
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                time = tempData;
            }
            else if (tempString.contains(GPS))
            {
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                GPSCoordinants = tempData;
            }
            else if (tempString.contains(PLOT_NUMBER))
            {
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                plotNumber = tempData;
            }
            else if (tempString.contains(OPERATOR_NAME))
            {
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                operatorName = tempData;
            }
            else if (tempString.contains(TEMPERATURE))
            {
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                temperature = tempData;
            }
            else if (tempString.contains(COMMENTS))
            {
                tempData = tempString.substring(tempString.lastIndexOf(COLON) + 2);
                comments = tempData;
            }
            else
            {
                //todo check for unwanted information
            }

            arrayCounter++;
        }
    }

    //is this supposed to be site name
    public String getSiteName()
    {
        return siteName;
    }

    public String getTime()
    {
        return time;
    }

    public String getGPS()
    {
        return GPSCoordinants;
    }

    public String getComments()
    {
        if (comments != "")
        {
            return comments;
        }
        else
        {
            return null;
        }
    }

    public String getPlotNumber()
    {
        if (plotNumber != "")
        {
            return plotNumber;
        }
        else
        {
            return null;
        }
    }

    public String getOpName()
    {
        if (operatorName != "")
        {
            return operatorName;
        }
        else
        {
            return null;
        }
    }

    public String getTemp()
    {
        if (temperature != "")
        {
            return temperature;
        }
        else
        {
            return null;
        }
    }

    //probably wrong getImage
    /*
    public BufferedImage getImage()
    {
        return image;
    }

    public LineGraphObject getGraph()
    {
        return graph;
    }
    */

    //main function for testing all functions
    public static void main(String args[])
    {
        String tempString = "Site Name: Big Tree Reading\nTime: 2018-02-14-16-30\nGPS: 35.184562, -111.651540\nPlot Number: 1\nOperator Name: Tim\nTemperature: 75\nComments: ";
        DataFolder temp = new DataFolder(tempString, tempString);
        //System.out.println(siteName + "\n" + time + "\n" + GPSCoordinants + "\n" + plotNumber + "\n" + operatorName + "\n" + temperature + "\n" + comments + "\n");

    }



}
