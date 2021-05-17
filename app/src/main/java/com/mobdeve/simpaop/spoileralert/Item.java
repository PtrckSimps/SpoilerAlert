package com.mobdeve.simpaop.spoileralert;

import android.graphics.Bitmap;

public class Item {

    private String itemName;
    private String itemCategory;
    private String itemExpDate;
    private int quantity;
    private Bitmap itemImage;
    private Bitmap proof;

    public Item(String itemName, String itemCategory, int quantity, String itemExpDate, Bitmap itemImage, Bitmap proof) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.quantity = quantity;
        this.itemExpDate = itemExpDate;
        this.itemImage = itemImage;
        this.proof = proof;
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
