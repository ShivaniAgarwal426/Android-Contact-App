package com.example.contactandroidapp.ContactTable.Model;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/** table for RoomDB
 * and
 * model for recyclerview
 */
//Entity class to store in Room Database
@Entity(tableName = "ContactTable")
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "firstname")
    public String firstname;
    @ColumnInfo(name = "lastname")
    public String lastname;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "address")
    public String address;

    public ContactEntity(int id, String firstname, String lastname, String email, String phone, String address) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

//    only the getters function for the adapter holders

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
