package com.omkarmhatre.inventory.application.Utils;

import com.omkarmhatre.inventory.application.Inventory.InventoryItem;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;

import java.util.ArrayList;
import java.util.List;

public class PriceBookService {

    private static PriceBookService instance;

    private List<PriceBookEntry> priceBook = new ArrayList<>() ;

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

    public void setPriceBook(List<PriceBookEntry> priceBook) {
        this.priceBook = priceBook;
    }

    public List<PriceBookEntry> getPriceBook() {
        return priceBook;
    }
}
