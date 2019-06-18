package com.example.contactlist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insert(ContactModel contact);

    @Update
    void update(ContactModel contact);

    @Delete
    void delete(ContactModel contact);

    @Query("DELETE FROM contact_table")
    void deleteAllContacts();

    @Query("SELECT * FROM contact_table ORDER BY name ASC ")
    LiveData<List<ContactModel>> getAllContacts();
}
