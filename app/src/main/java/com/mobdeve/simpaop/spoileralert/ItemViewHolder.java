package com.mobdeve.simpaop.spoileralert;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView itemImage;
    private TextView itemNameTv;
    private TextView itemCategoryTv;
    private TextView expiryTv;
    private TextView daysLeftTv;

    public ItemViewHolder(@NonNull View itemView){
        super(itemView);
        this.itemImage = itemView.findViewById(R.id.itemImage);
        this.itemNameTv = itemView.findViewById(R.id.itemNameTv);
        this.itemCategoryTv = itemView.findViewById(R.id.itemCategoryTv);
        this.expiryTv = itemView.findViewById(R.id.expiryTv);
        this.daysLeftTv = itemView.findViewById(R.id.daysLeftTv);
    }

    public void setItemName(String itemname){
        itemNameTv.setText(itemname);
    }

    public void setItemCategory(String category){
        itemCategoryTv.setText(category);
    }

    public void setExpiry(String expiry){
        expiryTv.setText(expiry);
    }

    public void setDaysLeft(int days){
       daysLeftTv.setText(String.valueOf(days));
    }

    public void setimage(int image){
        itemImage.setImageResource(image);
    }
}
