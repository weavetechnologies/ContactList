package com.example.contactlist;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Contact.class, version = 1)
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
          //  new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private ContactDao contactDao;

        private PopulateDbAsyncTask(ContactDatabase db) {
            contactDao = db.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.insert(new Contact("", "Karan", false));
            contactDao.insert(new Contact("", "Arjun", false));
            contactDao.insert(new Contact("", "Bunny", false));
            contactDao.insert(new Contact("", "Bakul", false));
            contactDao.insert(new Contact("", "Kirti", false));
            contactDao.insert(new Contact("", "Ajay", false));
            contactDao.insert(new Contact("", "Ajay34", false));
            contactDao.insert(new Contact("", "Ajay789", false));
            contactDao.insert(new Contact("", "Zen", false));
            contactDao.insert(new Contact("", "Barry", false));
            contactDao.insert(new Contact("", "Harish", false));
            contactDao.insert(new Contact("", "Harish", false));
            contactDao.insert(new Contact("", "Harish23", false));
            contactDao.insert(new Contact("", "Harish65", false));
            contactDao.insert(new Contact("", "Harish76", false));
            contactDao.insert(new Contact("", "Kamlesh", false));
            contactDao.insert(new Contact("", "Sumona", false));
            contactDao.insert(new Contact("", "Sumona23", false));
            contactDao.insert(new Contact("", "Sumona34", false));
            contactDao.insert(new Contact("", "Sumona546", false));
            return null;
        }
    }

}
