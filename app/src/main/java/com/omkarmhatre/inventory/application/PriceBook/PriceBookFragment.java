package com.omkarmhatre.inventory.application.PriceBook;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
 public class PriceBookFragment extends Fragment implements View.OnClickListener {

     @BindView(R.id.recyclerView)RecyclerView recyclerView;

     List<PriceBookEntry> priceBook = PriceBookService.getInstance().getPriceBook();
    PriceBookAdapter adapter;

    public PriceBookFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance() {
        PriceBookFragment fragment = new PriceBookFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_price_book, container, false);
        ButterKnife.bind(this,rootView);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, "Price Book"));

        setupRecyclerView(new LinearLayoutManager(container.getContext()));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        return rootView;
    }

    public void showPriceBook() {
        importPriceBook();
        adapter.notifyDataSetChanged();
    }

    public void importPriceBook() {


        PriceBookEntry pb = new PriceBookEntry("20","Omkar");
        priceBook.add(pb);
        priceBook.add(pb);
        priceBook.add(pb);

    }

    private void setupRecyclerView(LinearLayoutManager linearLayoutManager) {

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PriceBookAdapter(priceBook);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        showPriceBook();
        AppService.notifyUser(v,"Price Book Imported successfully.");

    }
}