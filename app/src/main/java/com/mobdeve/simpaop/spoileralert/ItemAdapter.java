package com.mobdeve.simpaop.spoileralert;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
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
        holder.setimage(itemArrayList.get(position).getItemImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ItemDetailsActivity.class);
                i.putExtra("NAME_KEY", itemArrayList.get(position).getItemName());
                i.putExtra("CATEGORY_KEY", itemArrayList.get(position).getItemCategory());
                i.putExtra("EXPIRY_KEY", itemArrayList.get(position).getItemExpDate());
                i.putExtra("QUANTITY_KEY", itemArrayList.get(position).getQuantity());
                //convert bitmap into byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                itemArrayList.get(position).getItemImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte [] byteArray = stream.toByteArray();
                i.putExtra("IMAGE_KEY", byteArray);
                itemArrayList.get(position).getProof().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                i.putExtra("PROOF_KEY", byteArray);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

}
