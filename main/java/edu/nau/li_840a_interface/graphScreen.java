/*
 *  Author: James Beasley
 *  Last updated: March 26th, 2018
 *  Description: Java file for the graph screen of the application. Contains methods which
 *               correspond to each button on the screen, as well as a receiver for data from the
 *               gas analyzer.
 */
package edu.nau.li_840a_interface;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Set;

public class graphScreen extends AppCompatActivity {

    private GraphView graphIds[];
    private TextView textIds[];
    private GraphManager manager;
    private Thread manager_thread;
    private SerialReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_screen);

        graphIds = new GraphView[4];
        graphIds[0] = findViewById(R.id.graph1);
        graphIds[1] = findViewById(R.id.graph2);
        graphIds[2] = findViewById(R.id.graph3);
        graphIds[3] = findViewById(R.id.graph4);

        textIds = new TextView[4];
        textIds[0] = findViewById(R.id.co2display);
        textIds[1] = findViewById(R.id.h2odisplay);
        textIds[2] = findViewById(R.id.tempdisplay);
        textIds[3] = findViewById(R.id.presdisplay);

        mHandler = new graphScreen.MyHandler(this);

        reader = new SerialReader();

        manager = new GraphManager(this, graphIds, textIds);
        manager_thread = new Thread(manager);
        manager_thread.start();

    }

    /*
     *  Runs when the "CO2" button is pressed. Hides every graph except for the CO2 one.
     */
    public void showCO2(View view)
    {

        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(0, 0));


    }

    /*
     *  Runs when the "H2O" button is pressed. Hides every graph except for the H2O one.
     */
    public void showH2O(View view)
    {

        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(0, 0));

    }

    /*
     *  Runs when the "Temp" button is pressed. Hides every graph except for the temperature one.
     */
    public void showTemp(View view)
    {

        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(0, 0));

    }

    /*
     *  Runs when the "Pressure" button is pressed. Hides every graph except for the pressure one.
     */
    public void showPres(View view)
    {

        graphIds[3].setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT));
        graphIds[0].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[1].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        graphIds[2].setLayoutParams(new LinearLayout.LayoutParams(0, 0));

    }

    /*
     *  Runs when either the "Start Logging" or "Stop Logging" buttons are pressed. Switches the
     *  text to the appropriate value and tells the graph manager to either start or stop recording
     *  incoming data points.
     */
    public void startStopLog(View view)
    {

        Button button = findViewById(R.id.logbutton);
        Button finalizeButton = findViewById(R.id.finalbutton);

        // If the button current says "Start Logging", switch the text and inform the manager
        if (button.getText().equals("Start Logging"))
        {
            button.setText("Stop Logging");
            finalizeButton.setBackgroundColor(Color.TRANSPARENT);
            finalizeButton.setEnabled(false);
            manager.resetGraphs();
            manager.startlogging();
        }

        // If the button currently says "Stop Logging", switch the text and inform the manager
        else
        {
            button.setText("Start Logging");
            if (!manager.isEmpty())
            {
                finalizeButton.setBackgroundResource(android.R.drawable.btn_default);
                finalizeButton.setEnabled(true);
            }
            manager.stoplogging();
        }

    }

    public void finalize(View view)
    {
        String reading;

        // Metadata values pulled from previous screen
        String metaSite;
        String metaOpName;
        String metaSampleId;
        String metaTemp;
        String metaComments;
        String metaTime;
        String metaString;
        String metaLong;
        String metaLat;
        String metaElevation;
        String imageString;
        String fileName;

        FileOutputStream outStream;

        Intent fileScreen;

        // Fetch the metadata values
        metaSite = getIntent().getStringExtra("SITE_NAME");
        metaOpName = getIntent().getStringExtra("OPERATOR_NAME");
        metaSampleId = getIntent().getStringExtra("SAMPLE_ID");
        metaTemp = getIntent().getStringExtra("TEMPERATURE");
        metaComments = getIntent().getStringExtra("COMMENTS");
        metaTime = getIntent().getStringExtra("TIME");
        metaLong = getIntent().getStringExtra("GPSLong");
        metaLat = getIntent().getStringExtra("GPSLat");
        metaElevation = getIntent().getStringExtra("ELEVATION");
        imageString = getIntent().getStringExtra("IMAGE");


        // Construct the CSV file content
        metaString = "Operator Name,Site Name,Sample ID,Temperature,Comments,Time and Date,GPS\n" +
                     metaOpName + "," + metaSite + "," + metaSampleId + "," + metaTemp + "," +
                     metaComments + "," + metaTime + "," + metaLong + "," + metaLat + "," + metaElevation;


        manager.stoplogging();
        manager.deconstruct();
        reader.communicating = false;

        reading = manager.toString();

        // Filename
        fileName = metaSite + "_" + metaSampleId + "_" + metaTime;

        try
        {
            // Graph File
            outStream = openFileOutput("G-" + fileName + ".csv", Context.MODE_APPEND);
            outStream.write(reading.getBytes());
            outStream.close();

            // Metadata File
            outStream = openFileOutput("M-" + fileName + ".csv", Context.MODE_APPEND);
            outStream.write(metaString.getBytes());
            outStream.close();

            // Image File
            if (!imageString.equals("NA"))
            {
                outStream = openFileOutput("I-" + fileName + ".png", Context.MODE_APPEND);
                stringToBitMap(imageString).compress(Bitmap.CompressFormat.PNG, 70, outStream);
                outStream.close();
            }

        }
        catch(Exception exception)
        {

        }

        fileScreen = new Intent(this, fileDirectory.class);
        startActivity(fileScreen);

    }

    private Bitmap stringToBitMap(String input)
    {
        try
        {
            byte[] encodedBytes = Base64.decode(input, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedBytes, 0, encodedBytes.length);
            return bitmap;
        }
        catch(Exception exception)
        {
            return null;
        }
    }

    /////////////////////
    // USB SERIAL CODE //
    //  DO NOT MODIFY  //
    /////////////////////

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    if (usbService != null)
                    {
                        String initMessage;
                        initMessage = "<LI840><CFG><OUTRATE>0.5</OUTRATE></CFG></LI840>\n";
                        usbService.write(initMessage.getBytes());
                    }
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UsbService usbService;
    private graphScreen.MyHandler mHandler;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<graphScreen> mActivity;

        public MyHandler(graphScreen activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    mActivity.get().reader.addChar(data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    ////////////////////////////
    // END OF USB SERIAL CODE //
    ////////////////////////////

    private class SerialReader
    {

        public String currentStream;
        public String completeStream;
        public boolean communicating;

        /*
         *
         */
        public SerialReader()
        {
            currentStream = "";
            completeStream = "";
            communicating = true;
        }

        public void addChar(String input)
        {

            if (input.equals("") || input.equals(" "))
            {
                return;
            }
            if (input.contains("\n"))
            {
                currentStream += input;
                completeStream = currentStream.trim();
                currentStream = "";
                manager.updateData(completeStream);
            }
            else
            {
                currentStream += input;
            }

        }

    }

    @Override
    public void onBackPressed()
    {

    }

}
