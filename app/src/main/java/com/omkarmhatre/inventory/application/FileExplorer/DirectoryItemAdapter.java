package com.omkarmhatre.inventory.application.FileExplorer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omkarmhatre.inventory.application.R;

import java.util.List;

public class DirectoryItemAdapter extends RecyclerView.Adapter {

    private List<DirectoryItem> contents;
    private Context context;


    public DirectoryItemAdapter(Context context, List<DirectoryItem> contents) {
        this.contents=contents;
        this.context=context;
    }

    @NonNull
    @Override
    public DirectoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =  LayoutInflater.from(viewGroup.getContext());
        View view =inflater.inflate(R.layout.layout_directory_item,null);
        DirectoryItemViewHolder viewHolder = new DirectoryItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ((DirectoryItemViewHolder)viewHolder).initiate(context,contents.get(position));

    }

    @Override
    public int getItemCount() {
        return contents.size();
    }
}
