package edu.nau.li_840a_interface;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import org.w3c.dom.Text;

public class fileDirectory extends AppCompatActivity implements OnClickListener {


    EditText editTextEmail, editTextSubject, editTextMessage;
    Button btnSend, btnView;
    private ListView lv;
    String email, subject, message, attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;
    ArrayList<Uri> listOfUri = new ArrayList<Uri>();
    public ArrayList<String> appFiles;
    int viewFilePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_directory);


        lv = (ListView) findViewById(R.id.listView);
        lv.setChoiceMode(2);

        lv.setChoiceMode(2);
        lv.setSelector(android.R.color.darker_gray);
        int i = 0;
        String metaCheck = "M-";


        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        appFiles = new ArrayList<String>();
        for (i = 0; i < context.getFilesDir().listFiles().length; i++) {
            if (context.getFilesDir().list()[i].contains(metaCheck)) {
                appFiles.add(context.getFilesDir().list()[i]);
            } else {
                //appFiles.add(context.getFilesDir().list()[i]);
            }

        }
        //put newest files at top
         Collections.reverse(appFiles);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
         ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                android.R.layout.simple_list_item_1,
                appFiles);

        lv.setAdapter(arrayAdapter);
        lv.getOnItemClickListener();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                File file = new File(context.getFilesDir(), lv.getItemAtPosition(i).toString());
              if(lv.isItemChecked(i) == false){
                adapterView.getChildAt(i).setBackgroundColor(Color.WHITE);
                  Uri uri = FileProvider.getUriForFile(context, "edu.nau.li_840a_interface", file);
                  listOfUri.remove(uri);}
               if(lv.isItemChecked(i) == true){
                    adapterView.getChildAt(i).setBackgroundColor(Color.BLUE);
                   Uri uri = FileProvider.getUriForFile(context, "edu.nau.li_840a_interface", file);
                    listOfUri.add(uri);
                    viewFilePos = i;
              }

                checkFileCount();

              /*
                for (int j = 0; j < context.getFilesDir().listFiles().length; j++) {
                    if (context.getFilesDir().list()[j].contains("2")) {
                        File file2 = new File(context.getFilesDir(), context.getFilesDir().list()[j]);
                        Uri uri2 = FileProvider.getUriForFile(context, "edu.nau.li_840a_interface", file2);
                        listOfUri.add(uri2);
                    }
                }*/


            }


        });


        btnSend = (Button) findViewById(R.id.btn_email);
        btnView = (Button) findViewById(R.id.btn_view);

        btnSend.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnView.setClickable(false);
        btnView.setBackgroundColor(Color.LTGRAY);

    }



    @Override
    public void onClick(View v) {

        if(v == btnView){
            Intent viewScreen;

            viewScreen = new Intent(this, viewScreen.class);
            viewScreen.putExtra("FILE",  lv.getItemAtPosition(viewFilePos).toString());

            startActivity(viewScreen);

        }

        if (v == btnSend) {
            try {

                //

                //makes file and its folder for testing
                Context context = this;




                File fileLocation = (context.getFilesDir().listFiles()[1]);
                File folderfile = (context.getFilesDir().listFiles()[0]);






                // Read the first line of the file
                //InputStreamReader inputReader = new InputStreamReader(inputStream);
                //BufferedReader buffReader = new BufferedReader(inputReader);
                //String line = buffReader.readLine();

                //if(testFolder.isDirectory()){
                //editTextMessage.setText(context.getFilesDir().list()[6]);}

                final Intent emailIntent = new Intent(

                        Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Uri uri = Uri.parse("content://" + fileLocation.getAbsolutePath());
                Uri uri = FileProvider.getUriForFile(this, "edu.nau.li_840a_interface", folderfile);
                Uri uri2 = FileProvider.getUriForFile(this,  "edu.nau.li_840a_interface", fileLocation);
                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, listOfUri.get(0), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }


                //listOfUri.add(uri);
                //listOfUri.add(uri2);
                emailIntent.setType("text/plain");

                //  emailIntent.putExtra(Intent.EXTRA_STREAM, uri2);
                emailIntent.putExtra(Intent.EXTRA_STREAM, listOfUri);


                if (URI != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                }
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                this.startActivity(Intent.createChooser(emailIntent,
                        "Sending email..."));

            } catch (Throwable t) {
                Toast.makeText(this,
                        "Request failed try again: " + t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

    }
    public void checkFileCount(){
        if (lv.getCheckedItemCount() != 1) {
            btnView.setClickable(false);
            btnView.setBackgroundColor(Color.TRANSPARENT);
        }
        else{ btnView.setClickable(true);
            btnView.setBackgroundColor(Color.LTGRAY);

        }

    }
}


