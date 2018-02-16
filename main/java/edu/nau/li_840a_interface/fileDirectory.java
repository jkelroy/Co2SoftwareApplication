package edu.nau.li_840a_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Andrew on 2/6/2018.
 */

public class fileDirectory extends AppCompatActivity{
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_directory);

        //arraylist of file names
        ArrayList<String> fileNames = new ArrayList<String>();
        //names for testing
        fileNames.add("Forest/00/2-16-2018/3:11:00");
        fileNames.add("Forest/01/2-16-2018/3:11:01");
        fileNames.add("Forest/02/2-16-2018/3:11:02");
        fileNames.add("Forest/03/2-16-2018/3:11:03");
        fileNames.add("Forest/04/2-16-2018/3:11:04");
        fileNames.add("Forest/10/2-16-2018/3:11:10");
        fileNames.add("Forest/11/2-16-2018/3:11:11");
        fileNames.add("Forest/12/2-16-2018/3:11:12");
        fileNames.add("Forest/13/2-16-2018/3:11:13");
        fileNames.add("Forest/14/2-16-2018/3:11:14");
        fileNames.add("Forest/00/2-16-2018/3:11:00");
        fileNames.add("Forest/01/2-16-2018/3:11:01");
        fileNames.add("Forest/02/2-16-2018/3:11:02");
        fileNames.add("Forest/03/2-16-2018/3:11:03");
        fileNames.add("Forest/04/2-16-2018/3:11:04");
        fileNames.add("Forest/10/2-16-2018/3:11:10");
        fileNames.add("Forest/11/2-16-2018/3:11:11");
        fileNames.add("Forest/12/2-16-2018/3:11:12");
        fileNames.add("Forest/13/2-16-2018/3:11:13");
        fileNames.add("Forest/14/2-16-2018/3:11:14");
        fileNames.add("Forest/00/2-16-2018/3:11:00");
        fileNames.add("Forest/01/2-16-2018/3:11:01");
        fileNames.add("Forest/02/2-16-2018/3:11:02");
        fileNames.add("Forest/03/2-16-2018/3:11:03");
        fileNames.add("Forest/04/2-16-2018/3:11:04");
        fileNames.add("Forest/10/2-16-2018/3:11:10");
        fileNames.add("Forest/11/2-16-2018/3:11:11");
        fileNames.add("Forest/12/2-16-2018/3:11:12");
        fileNames.add("Forest/13/2-16-2018/3:11:13");
        fileNames.add("Forest/14/2-16-2018/3:11:14");
        fileNames.add("Forest/00/2-16-2018/3:11:00");
        fileNames.add("Forest/01/2-16-2018/3:11:01");
        fileNames.add("Forest/02/2-16-2018/3:11:02");
        fileNames.add("Forest/03/2-16-2018/3:11:03");
        fileNames.add("Forest/04/2-16-2018/3:11:04");
        fileNames.add("Forest/10/2-16-2018/3:11:10");
        fileNames.add("Forest/11/2-16-2018/3:11:11");
        fileNames.add("Forest/12/2-16-2018/3:11:12");
        fileNames.add("Forest/13/2-16-2018/3:11:13");
        fileNames.add("Forest/14/2-16-2018/3:11:14");


        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(listAdapter);


    }

    public void goHomeScreen(View view)
    {

        Intent homeScreen;

        homeScreen = new Intent(this, homeScreen.class);

        startActivity(homeScreen);

    }

    public void goViewScreen(View view)
    {

        Intent viewScreen;

        viewScreen = new Intent(this, viewScreen.class);

        startActivity(viewScreen);

    }
}
