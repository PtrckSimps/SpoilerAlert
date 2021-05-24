package com.mobdeve.simpaop.spoileralert;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Item {

    //variables
    private int id;
    private String itemName;
    private String itemCategory;
    private String itemExpDate;
    private int quantity;
    private Bitmap itemImage;
    private Bitmap proof;

    //Item Constructor
    public Item(int id, String itemName, String itemCategory, int quantity, String itemExpDate, Bitmap proof, Bitmap itemImage) {
        this.id = id;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.quantity = quantity;
        this.itemExpDate = itemExpDate;
        this.itemImage = itemImage;
        this.proof = proof;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getItemExpDate() {
        return itemExpDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public Bitmap getProof() {
        return proof;
    }

    public int getDays(){
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
            date2 = stf.parse(itemExpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date2.getTime() - date1.getTime();
        long days = (diff / (1000*60*60*24));
        Math.abs(days);
        return (int) days;
    }

    public static Comparator<Item> ItemNameComparator = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getItemName().compareToIgnoreCase(o2.getItemName());
        }
    };

    public static Comparator<Item> ItemADaysComparator = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getDays() - o2.getDays();
        }
    };

    public static Comparator<Item> ItemDDaysComparator = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o2.getDays() - o1.getDays();
        }
    };
}
