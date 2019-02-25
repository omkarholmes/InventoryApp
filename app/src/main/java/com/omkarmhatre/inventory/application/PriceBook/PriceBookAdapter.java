package com.omkarmhatre.inventory.application.PriceBook;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omkarmhatre.inventory.application.R;

import java.util.List;

public class PriceBookAdapter extends RecyclerView.Adapter {

    List<PriceBookEntry> priceBookEntries;

    public PriceBookAdapter(List<PriceBookEntry> priceBookEntries) {
        this.priceBookEntries=priceBookEntries;
    }

    @NonNull
    @Override
    public PriceBookEntryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =  LayoutInflater.from(viewGroup.getContext());
        View view =inflater.inflate(R.layout.layout_price_book_entry,null);
        PriceBookEntryViewHolder viewHolder = new PriceBookEntryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ((PriceBookEntryViewHolder)viewHolder).initiate(priceBookEntries.get(position));

    }

    @Override
    public int getItemCount() {
        return priceBookEntries.size();
    }
}
