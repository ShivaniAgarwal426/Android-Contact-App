package com.example.contactandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

//import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.contactandroidapp.Adapter.ContactAdapter;
import com.example.contactandroidapp.ContactTable.ContactDatabase;
import com.example.contactandroidapp.ContactTable.Interface.ContactDao;
import com.example.contactandroidapp.ContactTable.Model.ContactEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAdd;
    RecyclerView contact_recyclerView;
    ContactAdapter adapter;
    // the DAO to access database
    ContactDao dao;
    ContactDatabase database;
    /**
     type to identity the intent if its for new contact or to edit contact
     **/
    private  static final String TYPE = "type";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/**  toolbar heading  **/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact App");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

//        2nd last
/**          IMP IMP IMP
          setup Database and get DAO      **/
        database = Room.databaseBuilder(this,ContactDatabase.class,"contactDB")
                .allowMainThreadQueries()
                .build();
        dao = database.getContactDao();


/**    setting layout and adapter   **/
        contact_recyclerView = findViewById(R.id.contact_list_rv);
        contact_recyclerView.setHasFixedSize(true);
        contact_recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

       // onResume() to show live data without using LiveData


/**  add contacts  **/
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this,AddEditContActivity.class).putExtra(TYPE,0));
            }
        });

        }

    @Override
    protected void onResume() {
        super.onResume();
        //        last
        List<ContactEntity> allContactList = dao.getAllContacts();
        Collections.reverse(allContactList);
//        set data from adapter using ContactEntity in recyclerview
        adapter = new ContactAdapter(this,allContactList);
        contact_recyclerView.setAdapter(adapter);

    }
}