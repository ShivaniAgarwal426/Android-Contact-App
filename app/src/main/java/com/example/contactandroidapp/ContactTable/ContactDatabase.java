package com.example.contactandroidapp.ContactTable;

//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contactandroidapp.ContactTable.Interface.ContactDao;
import com.example.contactandroidapp.ContactTable.Model.ContactEntity;

/**
1. set class as database
2. then make class as abstract
3. extend with RoomDatabase
4. call method of dao
 **/
@Database(entities = {ContactEntity.class}, version = 1)    //if  exportSchema is not provide in gradle dependency of app then set , exportSchema = false
public abstract class ContactDatabase  extends RoomDatabase {

    //   create Dao object of ContactDao
    public  abstract ContactDao getContactDao();

//    database ko dao me daalo fir usko table entity list me set kro

}
