package com.omkarmhatre.inventory.application.Inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private RelativeLayout view;
    private Activity activity;
    private InventoryItem inventoryItem;

    public InventoryItemViewHolder(Activity activity, @NonNull View itemView) {

        super(itemView);
        this.activity=activity;
        this.view=(RelativeLayout) itemView;

    }

    public void initiate(InventoryItem inventoryItem) {
        this.inventoryItem=inventoryItem;
       TextView upc = view.findViewById(R.id.upcCode);
       TextView description = view.findViewById(R.id.description);
       TextView quantity = view.findViewById(R.id.quantity);
       TextView previousQuantity = view.findViewById(R.id.previousQuantity);

       upc.setText(inventoryItem.getUpc());
       description.setText(inventoryItem.getDescription());
       quantity.setText(""+inventoryItem.getQuantity());
       previousQuantity.setText(""+inventoryItem.getLastQuantity());
       view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        showPrompt();

    }

    private void showPrompt() {




    }


}
