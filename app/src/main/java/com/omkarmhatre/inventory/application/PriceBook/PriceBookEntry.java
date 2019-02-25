package com.omkarmhatre.inventory.application.PriceBook;

public class PriceBookEntry {

    public String upc;
    public String description;

    public PriceBookEntry(String upc, String description) {
        this.upc = upc;
        this.description = description;
    }

    public String getUpc() {
        return upc;
    }

    public String getDescription() {
        return description;
    }
}
