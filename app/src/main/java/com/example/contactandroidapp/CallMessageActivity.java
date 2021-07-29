package com.example.contactandroidapp;

import androidx.annotation.Nullable;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.contactandroidapp.ContactTable.ContactDatabase;
import com.example.contactandroidapp.ContactTable.Interface.ContactDao;
import com.example.contactandroidapp.ContactTable.Model.ContactEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CallMessageActivity extends AppCompatActivity implements View.OnClickListener {
    TextView contact_name, contact_number, contact_email, contact_address;
    ImageView initial_img;
    Toolbar toolbar;
    Button call, message;
    //    for uploding images in contact
    ImageView uploadImage;
    FloatingActionButton addPhoto;
    final int[] selectedItem = {-1};
    private Uri uri;
    private Bitmap bitmap;
    /**
     * Code to access whether to select from gallery or to click image
     **/
    private static int CHOOSE_FROM_GALLERY_CODE = 1;
    private static int CLICK_IMAGE_CODE = 2;

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

        uploadImage = findViewById(R.id.upload_image);
        addPhoto = findViewById(R.id.addPhoto);

        call = findViewById(R.id.callbtn);
        message = findViewById(R.id.messagebtn);

//        onClick on uploadImage and addPhoto

        uploadImage.setOnClickListener(this);
        addPhoto.setOnClickListener(this);

//         on below line adding click listener for our calling image view.
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to make a call.
                if (contactDetails.getPhone().length() > 10) {
                    String callANumber = contactDetails.getPhone().substring(2).trim();
                    Log.i("call10digitNumber", callANumber);
                    makeCall(callANumber);
                } else {
                    makeCall(contactDetails.getPhone());
                }
            }
        });

        // on below line adding on click listener for our message image view.
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to send message
                sendMessage(contactDetails.getPhone().toString());
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
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + contactDetails.getFirstname().toString() + " " + contactDetails.getLastname() + "\nEmail: " + contactDetails.getEmail() + "\nContact Number: " + contactDetails.getPhone() + "\nAddress: " + contactDetails.getAddress());
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
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    private void makeCall(String contactNumber) {
        Log.i("callme", contactNumber);
        // on below line we are calling an intent to make a call.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // on below line we are setting data to it.
        callIntent.setData(Uri.parse("tel:" + contactNumber.trim()));
//        if(countDigitfor10(Integer.parseInt(contactNumber)) == 10){
        // on below line we are checking if the calling permissions are granted not.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//                return;
        } else {
            // at last we are starting activity to make phone call.
            startActivity(callIntent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == uploadImage || v == addPhoto) {
            // list of the items to be displayed to the user in the form of list so that user can select the item from
            final String[] list = new String[]{"Choose Image from Gallery", "Click and Upload"};

            AlertDialog.Builder upload_image_dialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_add_photo)
                    .setTitle("Upload Image")
                    .setSingleChoiceItems(list, selectedItem[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedItem[0] = which;    /** selectedItem[0] == 1st option , selectedItem[1] == 2nd option **/
                            Toast.makeText(CallMessageActivity.this, "Selected option " + list[which] + " " + selectedItem + " " + which, Toast.LENGTH_SHORT).show();
                            if (which == 0 && list[which] == "Choose Image from Gallery") {
//                                CallMessageActivity.this.onClick( );
                                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickImageIntent, CHOOSE_FROM_GALLERY_CODE);

                            }
                            if (which == 1 && list[which] == "Click and Upload") {
                                Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(captureImageIntent, CLICK_IMAGE_CODE);
                            }

                            dialog.dismiss();
                        }
                    });
            upload_image_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            // create and build the AlertDialog instance with the AlertDialog builder instance
            AlertDialog imageDialog = upload_image_dialog.create();
            imageDialog.show();

        }
    }
//    for image to be uploaded from gallery then uri else from capture than bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_FROM_GALLERY_CODE) {
                uri = data.getData();
                uploadImage.setImageURI(uri);
                return;
            }
            if (requestCode == CLICK_IMAGE_CODE) {
                bitmap = (Bitmap) data.getExtras().get("data");
                uploadImage.setImageBitmap(bitmap);
            }
        }
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