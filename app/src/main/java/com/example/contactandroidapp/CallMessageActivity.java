package com.example.contactandroidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

//import android.arch.persistence.room.Room;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.contactandroidapp.ContactTable.ContactDatabase;
import com.example.contactandroidapp.ContactTable.Interface.ContactDao;
import com.example.contactandroidapp.ContactTable.Model.ContactEntity;

public class CallMessageActivity extends AppCompatActivity {
    TextView contact_name, contact_number, contact_email, contact_address;
    ImageView initial_img;
    Toolbar toolbar;
    Button call, message;

    // the DAO to access database from table
    ContactEntity contactDetails;
    ContactDao dao;
    ContactDatabase database;

    private static final String ID = "id";
    private static final String TYPE = "type";
    private static int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_message);

        //        heading
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Saved Contact");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /**************************************************************************************************/

        // setup Database and get DAO
        database = Room.databaseBuilder(this, ContactDatabase.class, "contactDB")
                .allowMainThreadQueries()
                .build();
        dao = database.getContactDao();

        // get contact id  to edit
        Intent mIntent = getIntent();
        if (getIntent() != null) {
            id = getIntent().getIntExtra(ID, 0);
        }

//        initialize the widgets
        contact_name = findViewById(R.id.contact_name);
        contact_number = findViewById(R.id.contact_number);
        contact_email = findViewById(R.id.contact_email);
        contact_address = findViewById(R.id.contact_address);
        initial_img = findViewById(R.id.initial);

        call = findViewById(R.id.callbtn);
        message = findViewById(R.id.messagebtn);
//         on below line adding click listener for our calling image view.
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to make a call.
                 makeCall(contactDetails.getPhone());
            }
        });

        // on below line adding on click listener for our message image view.
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to send message
                 sendMessage(contactDetails.getPhone());
            }
        });

        /**  IMP IMP **/
//        get contact detail from entity table by id query in DAO
        contactDetails = dao.getItemById(id);
        contact_name.setText(contactDetails.getFirstname() + " " + contactDetails.getLastname());
        contact_number.setText(contactDetails.getPhone());
        contact_email.setText(contactDetails.getEmail());
        contact_address.setText(contactDetails.getAddress());


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();  // generate random color
        // below text drawable is a circular.
        TextDrawable drawable_initial = TextDrawable.builder().beginConfig()
                .width(100)  // width in px
                .height(100) // height in px
                .endConfig()
                .buildRound(contactDetails.getFirstname().substring(0, 1).toUpperCase() + contactDetails.getLastname().substring(0, 1).toUpperCase(), color);
        initial_img.setImageDrawable(drawable_initial);
    }


    //    for edit and delete icon on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "to edit contact", Toast.LENGTH_SHORT).show();
                // Edit the Contact by passing type=1 to AddEditContActivity
                // if intent type is 1
                Intent editIntent = new Intent(this, AddEditContActivity.class).putExtra(TYPE, 1);
                editIntent.putExtra(ID, contactDetails.getId());       // contact list me se id lelo and open kro to edit
                startActivity(editIntent);

                return true;
            case R.id.delete:
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(CallMessageActivity.this)
                        .setTitle("Contact will be Deleted")
                        .setIcon(R.drawable.ic_delete)
                        .setMessage("Do you want to delete " + contactDetails.getFirstname() + " " + contactDetails.getLastname() + " Number?")
                        .setCancelable(false)   //click out the alertbox still remain alert open
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**  IMP to DELETE Contact **/
                                dao.deleteContacts(contactDetails);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");    /** IMP IMP **/
                // Always use string resources for UI text.
//                contactDetails.getId();
                // Create intent to show chooser
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Name: "+contactDetails.getFirstname().toString()+" "+contactDetails.getLastname()+"\nEmail: "+contactDetails.getEmail()+"\nContact Number: "+contactDetails.getPhone()+"\nAddress: "+contactDetails.getAddress());
                Intent chooser = Intent.createChooser(shareIntent, "Share");
                // Try to invoke the intent.
                try {
                    startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    // Define what your app should do if no activity can handle the intent.
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendMessage(String contactNumber) {
        // in this method we are calling an intent to send sms.
        // on below line we are passing our contact number.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactNumber));
        intent.putExtra("sms_body", " ");
        startActivity(intent);
    }

    private void makeCall(String contactNumber) {
        // this method is called for making a call.
        // on below line we are calling an intent to make a call.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // on below line we are setting data to it.
        callIntent.setData(Uri.parse("tel:" + contactNumber));
        // on below line we are checking if the calling permissions are granted not.
        /**
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
         **/
        // at last we are starting activity.
        startActivity(callIntent);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        contactDetails = dao.getItemById(id);
//        contact_name.setText(contactDetails.getFirstname()+" "+contactDetails.getLastname());
//        contact_number.setText(contactDetails.getPhone());
//        contact_email.setText(contactDetails.getEmail());
//        contact_address.setText(contactDetails.getAddress());
//    }

}