package edu.nau.li_840a_interface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andrew on 2/6/2018.
 */

public class viewScreen extends AppCompatActivity {
    String metaData;
    String[] metaDataArray;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        //set up UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_screen);
        TextView siteTest = findViewById(R.id.tv_SiteName);
        TextView test2 = findViewById(R.id.textView11);
        //try to open file and save string metadata
        try {
            String metaFile = getIntent().getStringExtra("FILE");
            InputStream inputStream = openFileInput(metaFile);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(inputReader);
            metaData = buffReader.readLine();

        } catch(Exception e){
            siteTest.setText("Sorry there was an error opening your file, Please try again!");
        }
        //split metadata
        metaDataArray = StringParser.splitString(metaData);

        //put each string into text

        test2.setText("file name");
    }

}

//takes in string from file in format "col name, data\ncol name, data" and returns an array with
//raw data
class StringParser extends viewScreen{


    static String[] splitString(String metaData){
        String[] metaArray = new String[6];

        

        return metaArray;
    }
}
