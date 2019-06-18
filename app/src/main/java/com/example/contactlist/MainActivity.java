package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private ArrayList<ContactModel> contacts;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Contact");

        rvContacts = findViewById(R.id.rv_contacts);

        contacts = new ArrayList<>();

        contacts.add(new ContactModel("", "Arjun", false));
        contacts.add(new ContactModel("", "Bunny", false));
        contacts.add(new ContactModel("", "Bakul", false));
        contacts.add(new ContactModel("", "Kirti", false));
        contacts.add(new ContactModel("", "Ajay", false));
        contacts.add(new ContactModel("", "Zen", false));
        contacts.add(new ContactModel("", "Barry", false));
        contacts.add(new ContactModel("", "Harish", false));
        contacts.add(new ContactModel("", "Hari", false));
        contacts.add(new ContactModel("", "Kamlesh", false));
        contacts.add(new ContactModel("", "Yadav", false));
        contacts.add(new ContactModel("", "Karan", false));
        contacts.add(new ContactModel("", "Sumona", false));

        LiveData<List<ContactModel>> contactModels = new ContactRepo(getApplication()).getAllContacts();


        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        final ContactAdapter contactAdapter = new ContactAdapter();

        rvContacts.setAdapter(contactAdapter);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<ContactModel>>() {
            @Override
            public void onChanged(List<ContactModel> contactModels) {
                contactAdapter.submitList(contactModels);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                contactViewModel.delete(contactAdapter.getContactAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvContacts);
    }

    public ArrayList<ContactModel> getSortedContactsByName() {
        Collections.sort(contacts, ContactModel.nameComparator);
        return contacts;
    }
}
