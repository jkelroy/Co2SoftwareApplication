package edu.nau.li_840a_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jjoe64.graphview.GraphView;


/**
 * Created by Andrew on 2/6/2018.
 */

public class graphScreen  extends AppCompatActivity{

    private Thread thread;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        GraphManager manager;
        GraphView graphIds[];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_screen);

        graphIds = new GraphView[4];
        graphIds[0] = findViewById(R.id.graph1);
        graphIds[1] = findViewById(R.id.graph2);
        graphIds[2] = findViewById(R.id.graph3);
        graphIds[3] = findViewById(R.id.graph4);

        manager = new GraphManager(graphIds);

        //thread = new Thread(manager);

        //thread.start();

    }

    public void goFileDirectory(View view)
    {

        Intent fileDirectory;

        fileDirectory = new Intent(this, fileDirectory.class);

        startActivity(fileDirectory);

    }

}
