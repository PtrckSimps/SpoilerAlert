package com.mobdeve.simpaop.spoileralert;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private ImageView itemImage;
    private TextView itemNameTv;
    private TextView itemCategoryTv;
    private TextView expiryTv;
    private TextView daysLeftTv;
    private TextView quantityTv;
    private View viewBlock;
    private TextView daysTv;

    public ItemViewHolder(@NonNull View itemView){
        super(itemView);
        context = itemView.getContext();
        this.itemNameTv = itemView.findViewById(R.id.itemNameTv);
        this.itemCategoryTv = itemView.findViewById(R.id.itemCategoryTv);
        this.expiryTv = itemView.findViewById(R.id.expiryTv);
        this.quantityTv  = itemView.findViewById(R.id. quantityTv);
        this.daysLeftTv = itemView.findViewById(R.id.daysLeftTv);
        this.itemImage = itemView.findViewById(R.id.itemImage);
        this.quantityTv = itemView.findViewById(R.id.quantityTv);
        this.viewBlock = itemView.findViewById(R.id.daysColorView);
        this.daysTv = itemView.findViewById(R.id.daysTv);
    }

    public void setItemName(String itemname){
        itemNameTv.setText(itemname);
    }

    public void setItemCategory(String category){
        itemCategoryTv.setText(category);
    }

    public void setExpiry(String expiry){
        expiryTv.setText("Expiration: " + expiry);
    }

    public void setQuantity(int quantity){
        quantityTv.setText("Qty: " + String.valueOf(quantity));
    }

    public void setDaysLeft(String expiry) {
        String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        SimpleDateFormat stf =  new SimpleDateFormat("MM/dd/yyyy");

        Date date1 = null;
        try {
            date1 = stf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = stf.parse(expiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date2.getTime() - date1.getTime();
        long days = (diff / (1000*60*60*24));
        if(days <= 1){
            if(days == 1){
                daysLeftTv.setText(String.valueOf(days));
                daysTv.setText("day");
            }
            else{
                daysTv.setText("Expired");
                daysLeftTv.setText(String.valueOf(0));
            }
            daysTv.setTextColor(context.getColor(R.color.stop));
            viewBlock.setBackgroundResource(R.drawable.stop);

        }else if(days > 1 && days <= 3){
            daysTv.setTextColor(context.getColor(R.color.ready));
            viewBlock.setBackgroundResource(R.drawable.ready);
            daysLeftTv.setText(String.valueOf(days));
        }else{
            daysTv.setTextColor(context.getColor(R.color.go));
            viewBlock.setBackgroundResource(R.drawable.go);
            daysLeftTv.setText(String.valueOf(days));
        }
    }

    public void setimage(Bitmap image){
        itemImage.setImageBitmap(image);
    }
}
