package com.example.contactlist;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = ContactModel.class, version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    private static ContactDatabase instance;

    public abstract ContactDao contactDao();

    public static synchronized ContactDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class, "contact_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private ContactDao contactDao;

        private PopulateDbAsyncTask(ContactDatabase db) {
            contactDao = db.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.insert(new ContactModel("", "Karan", false));
            contactDao.insert(new ContactModel("", "Arjun", false));
            contactDao.insert(new ContactModel("", "Bunny", false));
            contactDao.insert(new ContactModel("", "Bakul", false));
            contactDao.insert(new ContactModel("", "Kirti", false));
            contactDao.insert(new ContactModel("", "Ajay", false));
            contactDao.insert(new ContactModel("", "Zen", false));
            contactDao.insert(new ContactModel("", "Barry", false));
            contactDao.insert(new ContactModel("", "Harish", false));
            contactDao.insert(new ContactModel("", "Kamlesh", false));
            contactDao.insert(new ContactModel("", "Sumona", false));
            return null;
        }
    }

}
