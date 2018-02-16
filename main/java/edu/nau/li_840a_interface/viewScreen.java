package edu.nau.li_840a_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andrew on 2/6/2018.
 */

public class viewScreen extends AppCompatActivity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_screen);
        TextView siteTest = (TextView) findViewById(R.id.tv_test);
        try {

            InputStream inputStream = openFileInput("testMeta.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(inputReader);
            String line = buffReader.readLine();
            siteTest.setText(line);

        } catch(Exception e){}
    }
}
