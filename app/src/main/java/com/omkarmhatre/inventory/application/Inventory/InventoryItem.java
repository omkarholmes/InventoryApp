package com.omkarmhatre.inventory.application.Inventory;

public class InventoryItem {

    private String upc;
    private String description;
    private int quantity;
    private int lastQuantity;

    public InventoryItem(String upc, String description, int quantity) {
        this.upc = upc;
        this.description = description;
        this.quantity = quantity;
    }

    public InventoryItem(String upc, String description) {
        this.upc = upc;
        this.description = description;
    }

    public String getUpc() {
        return upc;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(int lastQuantity) {
        this.lastQuantity = lastQuantity;
    }
}
