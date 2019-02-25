package com.omkarmhatre.inventory.application.Inventory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout view;

    public InventoryItemViewHolder(@NonNull View itemView) {

        super(itemView);
        this.view=(RelativeLayout) itemView;

    }

    public void initiate(InventoryItem inventoryItem) {

       TextView upc = view.findViewById(R.id.upcCode);
       TextView description = view.findViewById(R.id.description);
       TextView quantity = view.findViewById(R.id.quantity);
       TextView previousQuantity = view.findViewById(R.id.previousQuantity);

       upc.setText(inventoryItem.getUpc());
       description.setText(inventoryItem.getDescription());
       quantity.setText(""+inventoryItem.getQuantity());
       previousQuantity.setText(""+inventoryItem.getLastQuantity());

    }
}
