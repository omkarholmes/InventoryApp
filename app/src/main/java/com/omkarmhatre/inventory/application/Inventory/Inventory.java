package com.omkarmhatre.inventory.application.Inventory;

import android.view.View;

public interface Inventory {

    void updateInventoryList(InventoryItem newItem);
    void checkUpcInPriceBook(String upc);
    void addItemInInventoryList(View view);
    void clearData();

}
