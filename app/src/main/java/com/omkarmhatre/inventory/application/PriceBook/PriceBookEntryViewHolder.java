package com.omkarmhatre.inventory.application.PriceBook;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.R;

public class PriceBookEntryViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout view;

    public PriceBookEntryViewHolder(@NonNull View itemView) {

        super(itemView);
        this.view=(RelativeLayout) itemView;

    }

    public void initiate(PriceBookEntry priceBookEntry) {

       TextView upc = view.findViewById(R.id.upcCode);
       TextView description = view.findViewById(R.id.description);

       upc.setText(priceBookEntry.getUpc().replaceAll("\\s",""));
       description.setText(priceBookEntry.getDescription().trim());

    }
}
