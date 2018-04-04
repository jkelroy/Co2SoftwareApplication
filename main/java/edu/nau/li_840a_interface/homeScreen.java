/*
 *  Author: Andrew Greene
 *  Last updated: March 26th, 2018
 *  Description: Basic Point of navigation for our Application.  User can create a new data set, or
 *               view previously saved data sets.
 */

package edu.nau.li_840a_interface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class homeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    // The only Functions for this screen are Navigation Buttons.
    public void goNewDataSet(View view)
    {

        Intent metaData;

        metaData = new Intent(this, metaData.class);

        startActivity(metaData);

    }
    public void goFileDirectory(View view)
    {

        Intent fileDirectory;

        fileDirectory = new Intent(this, fileDirectory.class);

        startActivity(fileDirectory);

    }

    @Override
    public void onBackPressed()
    {

    }
}
