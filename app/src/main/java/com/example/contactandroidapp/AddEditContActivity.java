package com.example.contactandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

//import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactandroidapp.ContactTable.ContactDatabase;
import com.example.contactandroidapp.ContactTable.Interface.ContactDao;
import com.example.contactandroidapp.ContactTable.Model.ContactEntity;

public class AddEditContActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView firstName, lastName, email, phoneNumber, address;
    Button save, cancel;

    /**
    for intent identity if: add contact then type change and new id created
                        else: edit contact and id remain same
            this will done by setting type
     **/
    String ID = "id",
            TYPE = "type";    // 1st parameter in putExtra
    int id, type;             // 2nd parameter in putExtra
    boolean isEditing = false;

    // the DAO to access database
    ContactDatabase database;
    ContactDao dao;
    ContactEntity contactDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cont);

//        heading
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
         //added
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//        initialize
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phoneNumber =  findViewById(R.id.phone);
        address =  findViewById(R.id.address);
        save = findViewById(R.id.save_btn);
        cancel = findViewById(R.id.cancel_btn);

//        to acces dao from database we have to setup database n then get dao
        database = Room.databaseBuilder(this,ContactDatabase.class,"contactDB")
                .allowMainThreadQueries()
                .build();
        dao = database.getContactDao();


//        can add validation before saving
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                validate the fields

//                for editing
                if (isEditing){
                    ContactEntity contact = new ContactEntity(
                            contactDetail.getId(),
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            email.getText().toString().trim(),
                            phoneNumber.getText().toString().trim(),
                            address.getText().toString()
                    );
                   //if(not empty then save)
                    dao.updateContacts(contact);
                    finish();
                }
//                for creating new contact
                else {
                    ContactEntity contact = new ContactEntity(
                            0,
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            email.getText().toString().trim(),
                            phoneNumber.getText().toString().trim(),
                            address.getText().toString()
                    );
                    dao.insertContacts(contact);
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish / close the activity
                finish();
            }
        });


        // get the intent and check if it was editing existing contact or create a new one
        /** if intent is empty ----->> add using type  (0)
         else intent exist ------>> edit using type    (1)
         here id and type start with "0"
         if contact added then id increment
         **/
        if (getIntent() == null){
            getSupportActionBar().setTitle("Add Contact");
        }
        else if (getIntent() != null){
            id = getIntent().getIntExtra(ID,0);
            type = getIntent().getIntExtra(TYPE,0);
            if (type == 1){
                //saved contact will store in contact list using dao id
                //then display over the edit page when click on layout item of recycler view
                 isEditing = true;
                 getSupportActionBar().setTitle("Edit Contact");
                //access details of model by dao id
                contactDetail = dao.getItemById(id);
                firstName.setText(contactDetail.getFirstname());
                lastName.setText(contactDetail.getLastname());
                email.setText(contactDetail.getEmail());
                phoneNumber.setText(contactDetail.getPhone());
                address.setText(contactDetail.getAddress());
            }if(type == 0) {
                getSupportActionBar().setTitle("Add Contact");
            }
        }
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
                return true;
//            case R.id.share:
//                Toast.makeText(this, "Share to whatsapp", Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}