package com.mobdeve.simpaop.spoileralert;

public class Item {

    private String itemName;
    private String itemCategory;
    private String itemExpDate;
    private int itemImage;
    private int daysLeft;

    public Item(String itemName, String itemCategory, String itemExpDate, int daysLeft, int itemImage) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemExpDate = itemExpDate;
        this.itemImage = itemImage;
        this.daysLeft = daysLeft;
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

    public int getItemImage() {
        return itemImage;
    }

    public int getDaysLeft() {
        return daysLeft;
    }
}
