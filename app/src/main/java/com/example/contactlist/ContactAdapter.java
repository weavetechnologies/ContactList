package com.example.contactlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends ListAdapter<ContactModel, ContactAdapter.ContactHolder> {


    private OnItemClickListener listener;


    public ContactAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ContactModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ContactModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ContactModel oldItem, @NonNull ContactModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContactModel oldItem, @NonNull ContactModel newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getImageUrl().equals(newItem.getImageUrl());
        }
    };


    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ContactHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
        ContactModel contactModel = getItem(position);
        holder.ivFav.setVisibility(View.GONE);
        if (position == 0) {
            holder.tvInitial.setVisibility(View.VISIBLE);
            holder.tvInitial.setText(String.valueOf(contactModel
                    .getName()
                    .charAt(0)));
        } else {
            if (contactModel.getName().charAt(0) != getItem(position - 1).getName().charAt(0)) {
                holder.viewDivider.setVisibility(View.VISIBLE);
                holder.tvInitial.setVisibility(View.VISIBLE);
                holder.tvInitial.setText(String.valueOf(contactModel
                        .getName()
                        .charAt(0)));
            } else {
                holder.tvInitial.setVisibility(View.INVISIBLE);
                holder.viewDivider.setVisibility(View.INVISIBLE);
            }
        }
        holder.tvContactName.setText(contactModel.getName());

    }

    public ContactModel getContactAt(int position) {
        return getItem(position);
    }

    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvInitial;
        private ImageView ivFav;
        private RoundedImageView ivContactImage;
        private TextView tvContactName;
        private View viewDivider;


        public ContactHolder(View itemView) {
            super(itemView);
            tvInitial = itemView.findViewById(R.id.tv_initial);
            ivFav = itemView.findViewById(R.id.iv_fav);
            ivContactImage = itemView.findViewById(R.id.iv_contact_image);
            tvContactName = itemView.findViewById(R.id.tv_contact_name);
            viewDivider = itemView.findViewById(R.id.view_divider);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ContactModel contact);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

}
