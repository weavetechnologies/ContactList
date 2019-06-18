package com.example.contactlist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "contact_table")
public class ContactModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageUrl;
    private String name;
    private boolean isFav;

    public ContactModel(String imageUrl, String name, boolean isFav) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.isFav = isFav;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Comparator<ContactModel> nameComparator = new Comparator<ContactModel>() {
        @Override
        public int compare(ContactModel jc1, ContactModel jc2) {
            return (int) (jc1.getName().compareTo(jc2.getName()));
        }
    };
}
