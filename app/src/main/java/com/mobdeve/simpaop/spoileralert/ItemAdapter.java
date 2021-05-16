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
        holder.setQuantity(itemArrayList.get(position).getQuantity());
        holder.setDaysLeft(itemArrayList.get(position).getItemExpDate());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
