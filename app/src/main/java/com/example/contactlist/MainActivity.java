package com.example.contactlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.contactlist.Constants.CONTACT_ID;
import static com.example.contactlist.Constants.CONTACT_IMAGE_URL;
import static com.example.contactlist.Constants.CONTACT_IS_FAV;
import static com.example.contactlist.Constants.CONTACT_NAME;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvContacts;
    private RecyclerView rvFavContacts;
    private EditText etSearch;
    private ImageView ivSearch;
    private Button btnMatch;

    private int ADD_REQUEST_CODE = 101;
    private int EDIT_REQUEST_CODE = 102;

    private boolean isSearchOpen = false;

    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> favContacts = new CopyOnWriteArrayList<>();
    private List<Contact> searchedContacts = new ArrayList<>();
    //no arraylist, as it will throw ConcurrentModificationException
    // while deleting from main contacts

    private ContactViewModel contactViewModel;
    private ContactAdapter contactAdapter;
    private FavContactAdapter favContactAdapter;
    private String TAG = "K_LOG";
    private boolean isInitial = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle("Contact");

        rvContacts = findViewById(R.id.rv_contacts);
        rvFavContacts = findViewById(R.id.rv_fav_contacts);
        etSearch = findViewById(R.id.et_search);
        ivSearch = findViewById(R.id.iv_search);
        btnMatch = findViewById(R.id.btn_match);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvFavContacts.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> allContacts) {
                contacts = allContacts;
                setupContactAdapter(contacts);
                if (isInitial) {
                    getFavContacts();
                    isInitial = false;
                }

            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearchOpen) {
                    openSearchBar();
                } else {
                    closeSearchBar();
                }

            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvContacts.setAdapter(null);
                searchedContacts.clear();
                if (TextUtils.isEmpty(s.toString())) {
                    setupContactAdapter(contacts);
                    rvFavContacts.setVisibility(View.VISIBLE);
                } else {
                    rvFavContacts.setVisibility(View.GONE);
                    for (Contact contact : contacts) {
                        if (contact.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            searchedContacts.add(contact);
                            setupContactAdapter(searchedContacts);
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openSearchBar() {
        btnMatch.animate().alpha(0f).setDuration(500);

        etSearch.animate().alpha(1f).setDuration(450);
        etSearch.setVisibility(View.VISIBLE);
        Animation anim = new ScaleAnimation(
                0f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        etSearch.startAnimation(anim);
        isSearchOpen = true;
        btnMatch.setVisibility(View.GONE);
        etSearch.requestFocus();
        openKeyboard();

    }

    private void closeSearchBar() {
        closeKeyboard();
        btnMatch.animate().alpha(1f).setDuration(1000);
        btnMatch.setVisibility(View.VISIBLE);
        Animation anim = new ScaleAnimation(
                1f, 0f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        etSearch.startAnimation(anim);
        isSearchOpen = false;
        etSearch.animate().alpha(0f).setDuration(450);

        etSearch.setVisibility(View.INVISIBLE);
        etSearch.setText("");
    }


    private void setupContactAdapter(List<Contact> contacts) {
        contactAdapter = new ContactAdapter(contacts);
        contactAdapter.setOnItemClickListener(contactOnClick);
        rvContacts.setAdapter(contactAdapter);
    }

    private void getFavContacts() {
        for (Contact contact : contacts) {
            if (contact.isFav()) {
                favContacts.add(contact);
            }
        }
        favContactAdapter = new FavContactAdapter(favContacts);
        favContactAdapter.setOnItemClickListener(favContactOnClick);
        rvFavContacts.setAdapter(favContactAdapter);
    }

    private void findFromFavAndDelete(Contact contact) {
        for (Contact iContact : favContacts) {
            if (contact.getId() == iContact.getId()) {
                favContacts.remove(iContact);
                favContactAdapter.notifyDataSetChanged();
            }
        }
    }

    private void findFromFavAndEdit(Contact contact) {
        for (Contact iContact : favContacts) {
            if (contact.getId() == iContact.getId()) {
                iContact.setName(contact.getName());
                favContactAdapter.notifyDataSetChanged();
            }
        }
    }

    ContactAdapter.OnItemClickListener contactOnClick = new ContactAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

            Contact contact = contactAdapter.getContactAt(position);
            Intent i = new Intent(MainActivity.this, AddEditActivity.class);
            i.putExtra(CONTACT_ID, contact.getId());
            i.putExtra(CONTACT_NAME, contact.getName());
            startActivityForResult(i, EDIT_REQUEST_CODE);
            if (isSearchOpen) {
                closeSearchBar();
            }
        }

        @Override
        public void onDeleteClick(int position) {
            Contact contact = contactAdapter.getContactAt(position);
            findFromFavAndDelete(contact);
            contactViewModel.delete(contact);
            contacts.remove(position);
            contactAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAddToFavClick(int position) {
            Contact contact = contactAdapter.getContactAt(position);
            contact.setFav(true);
            contactViewModel.update(contact);
            contactAdapter.notifyItemChanged(position);
            favContacts.add(contact);
            favContactAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Contact Added To Favorites", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemoveFavClick(int position) {
            Contact contact = contactAdapter.getContactAt(position);
            findFromFavAndDelete(contact);
            contact.setFav(false);
            contactViewModel.update(contact);
            contactAdapter.notifyItemChanged(position);
            Toast.makeText(MainActivity.this, "Contact Removed From Favorites", Toast.LENGTH_SHORT).show();
        }
    };

    FavContactAdapter.OnItemClickListener favContactOnClick = new FavContactAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public void onRemoveFavClick(int position) {
            Contact contact = favContactAdapter.getContactAt(position);
            contact.setFav(false);
            contactViewModel.update(contact);
            favContacts.remove(position);
            contactAdapter.notifyDataSetChanged();
            favContactAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Contact Removed From Favorites", Toast.LENGTH_SHORT).show();
        }
    };


  /*  public ArrayList<Contact> getSortedContactsByName() {
        Collections.sort(contacts, Contact.nameComparator);
        return contacts;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact: {

                Intent i = new Intent(this, AddEditActivity.class);
                startActivityForResult(i, ADD_REQUEST_CODE);
                if (isSearchOpen) {
                    closeSearchBar();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void openKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Contact contact = new Contact("",
                        data.getStringExtra(CONTACT_NAME),
                        false);
                contactViewModel.insert(contact);
            }
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Contact contact = new Contact("",
                        data.getStringExtra(CONTACT_NAME),
                        false);
                contact.setId(data.getIntExtra(CONTACT_ID, -1));
                contactViewModel.update(contact);
                findFromFavAndEdit(contact);
            }
        }
    }

}
