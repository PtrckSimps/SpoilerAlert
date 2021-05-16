package com.mobdeve.simpaop.spoileralert;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{

    private ArrayList<Item> itemArrayList;

    public ItemAdapter(ArrayList<Item> data){
        this.itemArrayList = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_row, parent, false);

        ItemViewHolder holder = new ItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setItemName(itemArrayList.get(position).getItemName());
        holder.setItemCategory(itemArrayList.get(position).getItemCategory());
        holder.setExpiry(itemArrayList.get(position).getItemExpDate());
        holder.setDaysLeft(itemArrayList.get(position).getDaysLeft());
        holder.setimage(itemArrayList.get(position).getItemImage());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public void addItem(String itemName, String itemCategory, String expiry){
        itemArrayList.add(new Item(
                itemName, itemCategory,
                expiry,
                14, R.drawable.ic_launcher_background));
        notifyItemInserted(itemArrayList.size()-1);
    }
}
