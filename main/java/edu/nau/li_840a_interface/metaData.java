package edu.nau.li_840a_interface;

/**
 * Created by Andrew on 1/26/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;

import android.widget.*;

public class metaData extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meta_data);

    }


    public void goGraphScreen(View view)
    {
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        String site = siteName.getText().toString();
        try{
        //makes metadata file NAMES AFTER METADATA SITENAME
        File dir = new File(siteName.getText().toString());
        dir.mkdir();
        File testMeta = new File(dir, "testMeta.txt");

        FileOutputStream outputStream;
        outputStream = openFileOutput("testMeta.txt", Context.MODE_PRIVATE);
        outputStream.write(site.getBytes());

        } catch (Exception e){}


        Intent graphScreen;

        graphScreen = new Intent(this, graphScreen.class);

        startActivity(graphScreen);


    }


}
