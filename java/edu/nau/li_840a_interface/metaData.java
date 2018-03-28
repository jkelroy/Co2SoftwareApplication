/*
 *  Author: Andrew Greene
 *  Last updated: March 26th, 2018
 *  Description: Here the user enters in all of the Relative MetaData pertaining to the data set.
 *               The data is saved and passed to the next screen(s).  Background Operations are
 *               Time, Date, and GPS Autofill, and field validation
 */

package edu.nau.li_840a_interface;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class metaData extends AppCompatActivity {

    //Declaring Widget variables.
    ImageButton cameraB;
    private Bitmap image;
    private String currentDateTimeString;
    private String currentDateTimeFormatted;
    private static final int CAMERA_PIC_REQUEST = 2500;

    private Button GPS_b;
    private TextView GPS_tv;
    private LocationManager locationManager;
    private LocationListener listener;



    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmptyValues();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Date currentDate = new Date();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.meta_data);
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        EditText operatorName = (EditText) findViewById(R.id.et_ON);
        EditText sampleID = (EditText) findViewById(R.id.et_SID);
        EditText temperature = (EditText) findViewById(R.id.et_Temp);
        EditText comments = (EditText) findViewById(R.id.et_Com);


        //Disables button on Screen Creation
        operatorName.addTextChangedListener(textWatcher);
        siteName.addTextChangedListener(textWatcher);
        sampleID.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();


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
        imageB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });


        //***GPS***

        GPS_tv = (TextView) findViewById(R.id.tv_GPS);
        GPS_b = (Button) findViewById(R.id.GPSbutton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GPS_tv.setText("Longitude: " + location.getLongitude() + "; Latitude: " + location.getLatitude());
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
        GPS_b.performClick();
    }

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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CAMERA_PIC_REQUEST){

        }
        else {
            image = (Bitmap) data.getExtras().get("data"); //If the user hits the back button when Camera is open, data will be NULL and the app will crash
            ImageView imageview = (ImageView) findViewById(R.id.ImageView01);
            imageview.setImageBitmap(image);
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




    public void goGraphScreen(View view)
    {
        Context context = this;
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        EditText operatorName = (EditText) findViewById(R.id.et_ON);
        EditText sampleID = (EditText) findViewById(R.id.et_SID);
        EditText temperature = (EditText) findViewById(R.id.et_Temp);
        EditText comments = (EditText) findViewById(R.id.et_Com);
        TextView gps =  findViewById(R.id.tv_GPS);


        String site = siteName.getText().toString();
        String name = operatorName.getText().toString();
        String sampleid = sampleID.getText().toString();
        String temp = temperature.getText().toString();
        String commentS = comments.getText().toString();
        String gpsString = gps.getText().toString();

        String imageString = "NA";

        try
        {
            imageString = BitMapToString(image);
        } catch(Exception exception){
            //TODO Bring up a snackbar or message asking if they want to take a picture?
        }


        Intent graphScreen;

        graphScreen = new Intent(this, graphScreen.class);

        graphScreen.putExtra("SITE_NAME", site);
        graphScreen.putExtra("OPERATOR_NAME", name);
        graphScreen.putExtra("SAMPLE_ID", sampleid);
        graphScreen.putExtra("TEMPERATURE", temp);
        graphScreen.putExtra("COMMENTS", commentS );
        graphScreen.putExtra("IMAGE", imageString); // I have the reverse code (String to BitMap), I just don't know where you want it
        graphScreen.putExtra("TIME", currentDateTimeFormatted);
        graphScreen.putExtra("GPS", gpsString);


        startActivity(graphScreen);


    }


}
