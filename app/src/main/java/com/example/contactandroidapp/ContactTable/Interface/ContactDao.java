package com.example.contactandroidapp.ContactTable.Interface;

//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.OnConflictStrategy;
//import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactandroidapp.ContactTable.Model.ContactEntity;

import java.util.List;

//interface  for executing a Query of the table
@Dao
public interface ContactDao {
    /**
    Get all Contacts in database ordered by ASync
    @return a list with all Contacts
     * */
    @Query("SELECT * FROM ContactTable")
    List<ContactEntity> getAllContacts();

    /**
     Get all Contacts in database ordered by ID
     @return a Contacts
      * */
    @Query("SELECT * FROM CONTACTTABLE WHERE id=:id")
    ContactEntity getItemById(int id);

    /**
     Function to insert a contacts in room database
     @param contacts to be inserted in database
      * */
    @Insert
    //(onConflict = OnConflictStrategy)
    void insertContacts(ContactEntity contacts);

    /**
     Function to Update an contacts in room database
     @param contacts the object to be Update
      * */
    @Update
    void updateContacts(ContactEntity contacts);

    /**
     Function to delete an contacts in room database
      @param contacts the object to be deleted
      * */
    @Delete
    void deleteContacts(ContactEntity contacts);
}
