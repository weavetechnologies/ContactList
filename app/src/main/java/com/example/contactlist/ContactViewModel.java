package com.example.contactlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepo repository;
    private LiveData<List<ContactModel>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepo(application);
        allContacts = repository.getAllContacts();
    }

    public void insert(ContactModel contact) {
        repository.insert(contact);
    }

    public void update(ContactModel contact) {
        repository.update(contact);
    }

    public void delete(ContactModel contact) {
        repository.delete(contact);
    }

    public void deleteAllContacts() {
        repository.deleteAllContacts();
    }

    public LiveData<List<ContactModel>> getAllContacts() {
        return allContacts;
    }
}
