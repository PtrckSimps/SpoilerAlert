package com.mobdeve.simpaop.spoileralert;

import android.graphics.Bitmap;

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

}
