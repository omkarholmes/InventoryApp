package com.omkarmhatre.inventory.application.FileExplorer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;

public class DirectoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    RelativeLayout view;

    public DirectoryItemViewHolder(@NonNull View itemView) {

        super(itemView);
        this.view=(RelativeLayout) itemView;
        view.setOnClickListener(this);

    }

    public void initiate(Context context, DirectoryItem directoryItem) {

       ImageView icon = view.findViewById(R.id.dirItemIcon);
       TextView fileName = view.findViewById(R.id.dirItemText);

       icon.setImageDrawable(context.getDrawable(directoryItem.icon));
       fileName.setText(directoryItem.file);

    }

    @Override
    public void onClick(View v) {



    }
}
