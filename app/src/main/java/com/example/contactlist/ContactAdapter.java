package com.example.contactlist;

import android.util.Log;
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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {


    private List<Contact> contacts;
    private OnItemClickListener listener;

    public ContactAdapter(List<Contact> contacts) {
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
        holder.ivFav.setVisibility(View.GONE);
        if (position == 0) {
            holder.tvInitial.setVisibility(View.VISIBLE);
            holder.tvInitial.setText(String.valueOf(contact
                    .getName()
                    .charAt(0)));
        } else {
            if (contact.getName().charAt(0) != contacts.get(position - 1).getName().charAt(0)) {
                holder.viewDivider.setVisibility(View.VISIBLE);
                holder.tvInitial.setVisibility(View.VISIBLE);
                holder.tvInitial.setText(String.valueOf(contact
                        .getName()
                        .charAt(0)));
            } else {
                holder.tvInitial.setVisibility(View.INVISIBLE);
                holder.viewDivider.setVisibility(View.INVISIBLE);
            }
        }
        holder.tvContactName.setText(contact.getName());

        Log.d("K_LOG", "onBindViewHolder: " + contact.isFav());
       /* if (contact.isFav()) {
            holder.ivFav.setVisibility(View.VISIBLE);
        } else {
            holder.ivFav.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    Contact getContactAt(int position) {
        return contacts.get(position);
    }


    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            MenuItem.OnMenuItemClickListener {

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
                    MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

                    if (getContactAt(getAdapterPosition()).isFav()) {
                        MenuItem removeFav = menu.add(Menu.NONE, 2, 2, "Remove from Favorites");
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

                    } else {
                        MenuItem makeFav = menu.add(Menu.NONE, 2, 2, "Add to Favorites");
                        makeFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                                    listener.onAddToFavClick(getAdapterPosition());
                                    return true;
                                }
                                return false;
                            }
                        });

                    }

                    delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                                listener.onDeleteClick(getAdapterPosition());
                                return true;
                            }
                            return false;
                        }
                    });
                }
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            listener.onDeleteClick(position);
                            return true;
                        case 2:
                            listener.onAddToFavClick(position);
                            return true;
                    }
                }
            }
            return false;
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

        void onDeleteClick(int position);

        void onAddToFavClick(int position);

        void onRemoveFavClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

}
