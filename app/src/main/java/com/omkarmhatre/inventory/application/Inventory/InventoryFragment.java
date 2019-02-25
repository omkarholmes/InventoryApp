package com.omkarmhatre.inventory.application.Inventory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.PriceBook.PriceBookAdapter;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class InventoryFragment extends Fragment implements View.OnClickListener, TextWatcher{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.upcCode)EditText upcCode;
    @BindView(R.id.description)TextView description;
    @BindView(R.id.quantity)EditText quantity;
    @BindView(R.id.addItem)Button addItem;
    @BindView(R.id.section_label)TextView welcomeText;


    List<InventoryItem> inventoryList = new ArrayList<>();
    InventoryItemAdapter adapter;

    public InventoryFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        ButterKnife.bind(this,rootView);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format,"Inventory"));

        setupRecyclerView(new LinearLayoutManager(container.getContext()));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        addItem.setOnClickListener(this);

        upcCode.addTextChangedListener(this);

        return rootView;
    }

    public void showPriceBook() {
        importPriceBook();
        adapter.notifyDataSetChanged();
    }

    public void importPriceBook() {

        InventoryItem pb = new InventoryItem("20","Omkar",10);
        inventoryList.add(pb);
        inventoryList.add(pb);
        inventoryList.add(pb);

    }


    private void setupRecyclerView(LinearLayoutManager linearLayoutManager) {

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new InventoryItemAdapter(inventoryList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.fab :{
                break;
            }
            case R.id.addItem :{
                welcomeText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                InventoryItem item = new InventoryItem(upcCode.getText().toString(),description.getText().toString(),Integer.parseInt(quantity.getText().toString()));
                updateInventoryList(item);
                adapter.notifyDataSetChanged();
                clearData();
                break;
            }
        }
    }

    private void clearData() {
        upcCode.setText("");
        description.setText("");
        quantity.setText("");
    }

    private void updateInventoryList(InventoryItem newItem) {

        ListIterator<InventoryItem> iterator = inventoryList.listIterator();
        while(iterator.hasNext())
        {
            InventoryItem item = iterator.next();
            if(item.getUpc().equals(newItem.getUpc()))
            {
                newItem.setLastQuantity(item.getQuantity());
                newItem.setQuantity(newItem.getQuantity()+item.getQuantity());
                iterator.remove();
            }
            else
            {
                newItem.setLastQuantity(0);
            }
        }

        inventoryList.add(newItem);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
