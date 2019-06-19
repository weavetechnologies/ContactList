package com.example.contactlist;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavContactAdapter extends RecyclerView.Adapter<FavContactAdapter.ContactHolder> {


    private List<Contact> contacts;
    private OnItemClickListener listener;

    public FavContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ContactHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
        Contact contact = contacts.get(position);
        holder.tvInitial.setVisibility(View.GONE);
        holder.viewDivider.setVisibility(View.GONE);
        if (position == 0) {
            holder.ivFav.setVisibility(View.VISIBLE);
        } else {
            holder.ivFav.setVisibility(View.INVISIBLE);
        }
        holder.tvContactName.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    Contact getContactAt(int position) {
        return contacts.get(position);
    }


    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvInitial;
        private ImageView ivFav;
        private RoundedImageView ivContactImage;
        private TextView tvContactName;
        private View viewDivider;


        public ContactHolder(final View itemView) {
            super(itemView);
            tvInitial = itemView.findViewById(R.id.tv_initial);
            ivFav = itemView.findViewById(R.id.iv_fav);
            ivContactImage = itemView.findViewById(R.id.iv_contact_image);
            tvContactName = itemView.findViewById(R.id.tv_contact_name);
            viewDivider = itemView.findViewById(R.id.view_divider);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                    MenuItem removeFav = menu.add(Menu.NONE, 1, 1, "Remove from Favorites");
                    removeFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                                listener.onRemoveFavClick(getAdapterPosition());
                                return true;
                            }
                            return false;
                        }
                    });


                }
            });
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);


        void onRemoveFavClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

}
