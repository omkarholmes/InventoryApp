package com.omkarmhatre.inventory.application.Utils;

import com.omkarmhatre.inventory.application.Inventory.InventoryItem;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;

import java.util.List;

public class PriceBookService {

    private static PriceBookService instance;

    private List<PriceBookEntry> priceBook;

    public static PriceBookService getInstance()
    {
        if(instance == null)
        {
            instance = new PriceBookService();
        }
        return instance;
    }

    private PriceBookService() {

    }

    public InventoryItem checkInPriceBook(String upcCode)
    {
        InventoryItem inventoryItem=null;
        for(PriceBookEntry item : priceBook)
        {
            if(item.getUpc().equals(upcCode))
            {
                return inventoryItem = new InventoryItem(item.getUpc(),item.getDescription());
            }
        }

        return inventoryItem;
    }
}
