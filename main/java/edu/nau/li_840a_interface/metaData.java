package edu.nau.li_840a_interface;

/**
 * Created by Andrew on 1/26/2018.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import android.widget.*;
import java.util.Date;
import java.util.List;






public class metaData extends AppCompatActivity {


    ImageButton cameraB;
    private Bitmap image;
    private String currentDateTimeString;
    private static final int CAMERA_PIC_REQUEST = 2500;

    private Button b;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meta_data);
        TextView tv_Date;
        //***Time and Date***

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        tv_Date = (TextView) findViewById(R.id.tv_Date);
        //tv_Time = (TextView) findViewById(R.id.tv_Time);
        tv_Date.setText(currentDateTimeString);
        //tv_Time.setText(currentDateTimeString);

        //***Camera***

        ImageButton imageB = (ImageButton) findViewById(R.id.cameraB);
        imageB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });


        //***GPS***

        t = (TextView) findViewById(R.id.tv_GPS);
        b = (Button) findViewById(R.id.GPSbutton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                t.setText("\n Longitude: " + location.getLongitude() + "\n Latitude: " + location.getLatitude());
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
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IGNORE THIS ERROR.  App still runs, Android Studio thinks I am not checking permissions,
                //but in reality I am Checking Permissions in the if statement above (noinspection MissingPermission)
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            image = (Bitmap) data.getExtras().get("data"); //If the user hits the back button when Camera is open, data will be NULL and the app will crash
            //Drawable bitdraw = new BitmapDrawable(getApplicationContext().getResources(),image); //not possible..
            ImageView imageview = (ImageView) findViewById(R.id.ImageView01);
            imageview.setImageBitmap(image);


        }
    }


    public void goGraphScreen(View view)
    {
        Context context = this;
        EditText siteName = (EditText) findViewById(R.id.et_SN);
        EditText operatorName = (EditText) findViewById(R.id.et_ON);
        EditText sampleID = (EditText) findViewById(R.id.et_SID);
        EditText temperature = (EditText) findViewById(R.id.et_Temp);
        EditText comments = (EditText) findViewById(R.id.et_Com);

        String site = siteName.getText().toString();
        String name = operatorName.getText().toString();
        String sampleid = sampleID.getText().toString();
        String temp = temperature.getText().toString();
        String commentS = comments.getText().toString();

        Intent graphScreen;

        graphScreen = new Intent(this, graphScreen.class);

        graphScreen.putExtra("SITE_NAME", site);
        graphScreen.putExtra("OPERATOR_NAME", name);
        graphScreen.putExtra("SAMPLE_ID", sampleid);
        graphScreen.putExtra("TEMPERATURE", temp);
        graphScreen.putExtra("COMMENTS", commentS );
        graphScreen.putExtra("PICTURE", image); // TODO: This might work???
        graphScreen.putExtra("TIME", currentDateTimeString);


        startActivity(graphScreen);


    }


}
