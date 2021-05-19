package com.mobdeve.simpaop.spoileralert;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> implements Filterable {

    private ArrayList<Item> itemArrayList;
    private ArrayList<Item> itemArrayListFull;


    public ItemAdapter(ArrayList<Item> data){
        this.itemArrayList = data;
        itemArrayListFull = new ArrayList<>(data);
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
                i.putExtra("ROWID", itemArrayList.get(position).getId());
                /*
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
                */
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        // constraint refers to newText on searchView
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Item> filteredList = new ArrayList<>();
            //Adds all items to the list since the search bar is empty
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(itemArrayListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Item item : itemArrayListFull){
                    // adds item if item contains the filterPattern
                    if(item.getItemName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        //Updates arraylist with results from filter
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemArrayList.clear();
            itemArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}
