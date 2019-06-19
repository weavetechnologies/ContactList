package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.contactlist.Constants.CONTACT_ID;
import static com.example.contactlist.Constants.CONTACT_IMAGE_URL;
import static com.example.contactlist.Constants.CONTACT_IS_FAV;
import static com.example.contactlist.Constants.CONTACT_NAME;

public class AddEditActivity extends AppCompatActivity {

    private RoundedImageView ivContact;
    private EditText etName;
    private Button btnAdd;

    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        ivContact = findViewById(R.id.iv_contact);
        etName = findViewById(R.id.et_name);
        btnAdd = findViewById(R.id.btn_add);


        if (getIntent().hasExtra(CONTACT_ID)) {
            isEdit = true;
            setTitle("Edit Contact");
            etName.setText(getIntent().getStringExtra(CONTACT_NAME));
            etName.setSelection(etName.getText().length());
            btnAdd.setText("Edit");
        } else {
            setTitle("Add Contact");
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    Intent i = new Intent(AddEditActivity.this, MainActivity.class);
                    if (isEdit) {
                        i.putExtra(CONTACT_ID, getIntent().getIntExtra(CONTACT_ID, -1));
                    }
                    i.putExtra(CONTACT_NAME, name.substring(0, 1).toUpperCase() + name.substring(1));
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(AddEditActivity.this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
