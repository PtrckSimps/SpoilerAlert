package com.mobdeve.simpaop.spoileralert;

public class Item {

    private String itemName;
    private String itemCategory;
    private String itemExpDate;
    private int quantity;
    private byte[] itemImage;
    private byte[] proof;

    public Item(String itemName, String itemCategory, int quantity, String itemExpDate, byte[] itemImage, byte[] proof) {
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

    public byte[] getItemImage() {
        return itemImage;
    }

    public byte[] getProof() {
        return proof;
    }
}
