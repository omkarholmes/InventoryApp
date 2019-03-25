package com.omkarmhatre.inventory.application.PriceBook;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.FileExplorer.FileExplorer;
import com.omkarmhatre.inventory.application.FileExplorer.FileExplorerFragment;
import com.omkarmhatre.inventory.application.Inventory.InventoryFragment;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
 public class PriceBookFragment extends Fragment implements View.OnClickListener {

     @BindView(R.id.recyclerView)RecyclerView recyclerView;
     @BindView(R.id.section_label)LinearLayout introText;

     private static PriceBookFragment fragment;
     List<PriceBookEntry> priceBook =PriceBookService.getInstance().getPriceBook();
    PriceBookAdapter priceBookListAdapter;


    public PriceBookFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance() {
        if(fragment == null) {
            fragment=new PriceBookFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_price_book, container, false);
        ButterKnife.bind(this,rootView);
        PriceBookService.getInstance().instantiate(getActivity(),this,getContext());
        setupRecyclerView(new LinearLayoutManager(container.getContext()));

        /*FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);*/

        PriceBookService.getInstance().loadFileList();
        //showDialog(DIALOG_LOAD_FILE);
        if(!PriceBookService.getInstance().getPriceBook().isEmpty())
        {
            showPriceBook();
        }

        return rootView;
    }

    private void setupRecyclerView(LinearLayoutManager linearLayoutManager) {

        recyclerView.setLayoutManager(linearLayoutManager);
        priceBookListAdapter = new PriceBookAdapter(priceBook);
        recyclerView.setAdapter(priceBookListAdapter);

    }

    //Code to import PriceBook File
    public void showPriceBook() {
        priceBook = PriceBookService.getInstance().getPriceBook();
        priceBookListAdapter.notifyDataSetChanged();
        introText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        PriceBookService.getInstance().showDialog();

        //startActivity(new Intent(getContext(),BluetoothConnector.class));
    }
}