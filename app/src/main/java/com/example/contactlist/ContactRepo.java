package com.example.contactlist;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactRepo {
    private ContactDao contactDao;
    private LiveData<List<ContactModel>> contacts;

    public ContactRepo(Application application) {
        ContactDatabase database = ContactDatabase.getInstance(application);
        contactDao = database.contactDao();
        contacts = contactDao.getAllContacts();
    }

    public void insert(ContactModel contact) {
        new InsertContactAsyncTask(contactDao).execute(contact);
    }


    public void update(ContactModel contact) {
        new UpdateContactAsyncTask(contactDao).execute(contact);
    }


    public void delete(ContactModel contact) {
        new DeleteContactAsyncTask(contactDao).execute(contact);

    }


    public void deleteAllContacts() {
        new DeleteAllContactAsyncTask(contactDao).execute();

    }


    public LiveData<List<ContactModel>> getAllContacts() {
        return contacts;
    }

    private static class InsertContactAsyncTask extends AsyncTask<ContactModel, Void, Void> {
        private ContactDao contactDao;

        private InsertContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(ContactModel... contactModels) {
            contactDao.insert(contactModels[0]);
            return null;
        }
    }


    private static class UpdateContactAsyncTask extends AsyncTask<ContactModel, Void, Void> {
        private ContactDao contactDao;

        private UpdateContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(ContactModel... contactModels) {
            contactDao.update(contactModels[0]);
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<ContactModel, Void, Void> {
        private ContactDao contactDao;

        private DeleteContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(ContactModel... contactModels) {
            contactDao.delete(contactModels[0]);
            return null;
        }
    }

    private static class DeleteAllContactAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactDao contactDao;

        private DeleteAllContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.deleteAllContacts();
            return null;
        }
    }
}
