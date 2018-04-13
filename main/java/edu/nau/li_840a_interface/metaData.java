/*
 *  Author: Andrew Greene
 *  Last updated: April 14th, 2018
 *  Description: Here the user enters in all of the Relative MetaData pertaining to the data set.
 *               The data is saved and passed to the next screen(s).  Background Operations are
 *               Time, Date, and GPS Autofill, and field validation
 */

package edu.nau.li_840a_interface;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.location.Location;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AuthProvider;
import java.text.DateFormat;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class metaData extends AppCompatActivity {
    //private AutoCompleteTextView et_ON;
    //private EditText et_SN;
    //private EditText et_SID;
    //private EditText et_Temp;
    //static final String STATE_KEY = "stateKey";
    //String[] names = getResources().getStringArray(R.array.names_array);

    AutoCompleteTextView operatorName;

    // Camera Variables
    ImageButton cameraB;
    private static final int REQUEST_CODE = 1;
    public Bitmap image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imagePreview;

    // Time/Date Variables
    private String currentDateTimeString;
    private String currentDateTimeFormatted;

    //GPS Variables
    private Button GPS_b;
    private EditText et_GPSLong;
    private EditText et_GPSLat;
    private EditText et_Elevation;
    private LocationManager locationManager;
    private LocationListener listener;


    // Text Watcher Handles Field Validation, checks after the Text has been changed, Raises Toast
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmptyValues();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Load Saved State, Used for Autofill

        if (savedInstanceState != null) {
            //mState = (String) savedInstanceState.get(STATE_KEY);
            //et_ON.setText("TEST");
            //CharSequence test = savedInstanceState.getCharSequence("op_string");
            //operatorName.setText(test);
            Log.d("SAVE", "Saved Instance State is not null");
        }

        setContentView(R.layout.meta_data);

        // Instance Variables by ID
        Date currentDate = new Date();
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        //operatorName = (AutoCompleteTextView) findViewById(R.id.et_ON);
        operatorName = (AutoCompleteTextView) findViewById(R.id.et_ON);

        EditText sampleID = (EditText) findViewById(R.id.et_SID);
        EditText temperature = (EditText) findViewById(R.id.et_Temp);
        EditText comments = (EditText) findViewById(R.id.et_Com);
        imagePreview = findViewById(R.id.imageView);

        // Initiates field Validation, Disables "Finish" button on Screen Creation
        //operatorName.addTextChangedListener(textWatcher);
        siteName.addTextChangedListener(textWatcher);
        sampleID.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();




        // AutoFill
        // Get a reference to the AutoCompleteTextView in the layout
        // Get the string array
        String[] names = getResources().getStringArray(R.array.names_array);
        // Create the adapter and set it to the AutoCompleteTextView

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        operatorName.setAdapter(adapter);

        //***Time and Date***
        TextView tv_Date;

        // Get time
        currentDateTimeString = DateFormat.getDateTimeInstance().format(currentDate);

        // Set time on screen
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        tv_Date = (TextView) findViewById(R.id.tv_Date);
        tv_Date.setText(currentDateTimeString);

        // Format time to filename
        DateFormat dateAndTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        currentDateTimeFormatted = dateAndTimeFormat.format(currentDate);


        //***Camera***

        ImageButton imageB = (ImageButton) findViewById(R.id.cameraB);


        //***GPS***

        et_GPSLong =  findViewById(R.id.et_GPSLong);
        et_GPSLat =  findViewById(R.id.et_GPSLat);
        et_Elevation =  findViewById(R.id.et_Elevation);

        GPS_b = (Button) findViewById(R.id.GPSbutton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // GPS Listener
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                et_GPSLong.setText(""+ location.getLongitude());
                et_GPSLat.setText("" + location.getLatitude());
                et_Elevation.setText("" + location.getAltitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();
        // Initiates GPS Search on Create.
        GPS_b.performClick();




    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //String Operator = operatorName.getString;
        //savedInstanceState.putString(STATE_KEY, mState);
        //.putCharSequence("op_string", Operator);
        //Log.d("SAVE", Operator+ " as Saved");


        //outState.putString(SN_KEY, et_SN.getText().toString());
        //outState.putString(T_KEY, et_Temp.getText().toString());

        // call superclass to save any view hierarchy
    }

    // Restore Save State
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //et_ON.setText("TEST");
        //operatorName = (AutoCompleteTextView) findViewById(R.id.et_ON);
        //operatorName.setText(savedInstanceState.getString("op_string"));
        Log.d("SAVE", " Was Restored");

        //et_SN.setText(savedInstanceState.getString(SN_KEY));
        //et_Temp.setText(savedInstanceState.getString(T_KEY));

    }

    // Requesting Permissions for GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    // Starts On click Listener for GPS
    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        GPS_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // IGNORE THIS ERROR.  App still runs, Android Studio thinks I am not checking permissions,
                // but in reality I am Checking Permissions in the if statement above
                // (noinspection MissingPermission)
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
    }

    //***Camera***
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image = (Bitmap) data.getExtras().get("data");
            imagePreview.setImageBitmap(imageBitmap);
        }
    }


    //***Field Validation***

    private  void checkFieldsForEmptyValues(){
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        EditText operatorName = (EditText) findViewById(R.id.et_ON);
        EditText sampleID = (EditText) findViewById(R.id.et_SID);
        EditText temperature = (EditText) findViewById(R.id.et_Temp);
        EditText comments = (EditText) findViewById(R.id.et_Com);
        Button validate = (Button) findViewById(R.id.b_finish);

        String s1 = operatorName.getText().toString();
        String s2 = siteName.getText().toString();
        String s3 = sampleID.getText().toString();
        String s4 = temperature.getText().toString();
        String s5 = comments.getText().toString();

        if(s1.equals("") || s2.equals("") || s3.equals(""))
        {
            validate.setEnabled(false);
        }
        else if(s2.contains(" ")){
            Toast toast1 = Toast.makeText(getApplicationContext(), "Site Name cannot use spaces", Toast.LENGTH_SHORT);
            toast1.show();
            validate.setEnabled(false);
        }
        else if(s3.contains(" ")){
            Toast toast2 = Toast.makeText(getApplicationContext(), "Sample ID cannot use spaces", Toast.LENGTH_SHORT);
            toast2.show();
            validate.setEnabled(false);
        }

        else
        {
            validate.setEnabled(true);
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    // Disables Back Button
    @Override
    public void onBackPressed()
    {

    }




    // "FINISH" Button, goes to the next screen to start a new data set
    public void goGraphScreen(View view)
    {
        String[] passingValues = new String[8];
        int counter;
        Intent graphScreen;
        //Context context = this; // This might be unnecessary

        // Fetch all the text boxes
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        AutoCompleteTextView operatorName = findViewById(R.id.et_ON);
        EditText sampleID = (EditText) findViewById(R.id.et_SID);
        EditText temperature = (EditText) findViewById(R.id.et_Temp);
        EditText comments = (EditText) findViewById(R.id.et_Com);
        EditText longitude = findViewById(R.id.et_GPSLong);
        EditText latitude = findViewById(R.id.et_GPSLat);
        EditText elevation = findViewById(R.id.et_Elevation);

        // Get each string value from the text boxes and place them in an array
        passingValues[0] = siteName.getText().toString();
        passingValues[1] = operatorName.getText().toString();
        passingValues[2] = sampleID.getText().toString();
        passingValues[3] = temperature.getText().toString();
        passingValues[4] = comments.getText().toString();
        passingValues[5] = longitude.getText().toString();
        passingValues[6] = latitude.getText().toString();
        passingValues[7] = elevation.getText().toString();

        // Check each value in the array for null or empty strings
        for (counter = 0; counter < passingValues.length; counter++)
        {

            // If one is a null value or empty string, set it to the string "NULL"
            if (passingValues[counter] == null || passingValues[counter].equals(""))
            {
                passingValues[counter] = "NULL";
            }

        }


        String imageString = "NA";
        try
        {
            imageString = BitMapToString(image);
        } catch(Exception exception){
            //TODO Bring up a snackbar or message asking if they want to take a picture?
        }

        // Initialize the graph screen
        graphScreen = new Intent(this, graphScreen.class);

        // Bundle in our array values to the graph screen
        graphScreen.putExtra("SITE_NAME", passingValues[0]);
        graphScreen.putExtra("OPERATOR_NAME", passingValues[1]);
        graphScreen.putExtra("SAMPLE_ID", passingValues[2]);
        graphScreen.putExtra("TEMPERATURE", passingValues[3]);
        graphScreen.putExtra("COMMENTS", passingValues[4]);
        graphScreen.putExtra("IMAGE", imageString );
        graphScreen.putExtra("TIME", currentDateTimeFormatted);
        graphScreen.putExtra("GPSLong", passingValues[5]);
        graphScreen.putExtra("GPSLat", passingValues[6]);
        graphScreen.putExtra("ELEVATION", passingValues[7]);

        // Start the graph screen
        startActivity(graphScreen);

    }

    public void goHomeScreen(View view)
    {

        Intent homeScreen;

        homeScreen = new Intent(this, homeScreen.class);

        startActivity(homeScreen);

    }


}
