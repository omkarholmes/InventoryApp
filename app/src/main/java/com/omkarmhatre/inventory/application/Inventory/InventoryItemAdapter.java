package com.omkarmhatre.inventory.application.Inventory;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;

import java.util.List;

public class InventoryItemAdapter extends RecyclerView.Adapter {

    List<InventoryItem> inventoryItemList;
    Activity activity;

    public InventoryItemAdapter(Activity activity, List<InventoryItem> inventoryItemList) {
        this.activity=activity;
        this.inventoryItemList=inventoryItemList;
    }

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =  LayoutInflater.from(viewGroup.getContext());
        View view =inflater.inflate(R.layout.layout_item_entry,null);
        InventoryItemViewHolder viewHolder = new InventoryItemViewHolder(activity,view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ((InventoryItemViewHolder)viewHolder).initiate(inventoryItemList.get(position));

    }

    @Override
    public int getItemCount() {
        return inventoryItemList.size();
    }
}
