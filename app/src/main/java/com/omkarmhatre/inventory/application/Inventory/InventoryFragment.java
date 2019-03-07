package com.omkarmhatre.inventory.application.Inventory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.DashboardActivity;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookAdapter;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;
import com.omkarmhatre.inventory.application.VoiceListener.SpeechListenerService;
import com.omkarmhatre.inventory.application.VoiceListener.VoiceListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("ValidFragment")
public class InventoryFragment extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    @BindView(R.id.recyclerView)RecyclerView recyclerView;
    @BindView(R.id.upcCode)EditText upcCode;
    @BindView(R.id.description)TextView description;
    @BindView(R.id.quantity)EditText quantity;
    @BindView(R.id.addItem)Button addItem;
    @BindView(R.id.section_label)LinearLayout introText;
    @BindView(R.id.fab)FloatingActionButton fab;


    List<InventoryItem> inventoryList = new ArrayList<>();
    InventoryItemAdapter adapter;
    DashboardActivity activity;


    public InventoryFragment(DashboardActivity activity) {
        this.activity=activity;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(DashboardActivity activity) {
        InventoryFragment fragment = new InventoryFragment(activity);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        ButterKnife.bind(this,rootView);
        upcCode.requestFocus();
        setupRecyclerView(new LinearLayoutManager(container.getContext()));

        setOnClickListeners();
        setupTextWatcher();

        return rootView;
    }

    private void setOnClickListeners() {
        fab.setOnClickListener(this);
        addItem.setOnClickListener(this);
        quantity.setOnClickListener(this);
    }

    private void setupTextWatcher() {

        upcCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                {
                    return;
                }
                checkUpcInPriceBook(s.toString());
                /*if(!PriceBookService.getInstance().getPriceBook().isEmpty())
                {
                    checkUpcInPriceBook(s.toString());
                }*/

            }
        });


        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                {
                    return;
                }
                addItemInInventoryList(quantity);
            }
        });
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
                //Todo : remove below code and change to add into price book
                //SpeechListenerService.start(this,getContext());
                //startActivity(new Intent(getContext(),VoiceListener.class));
                activity.startListening();
                break;
            }
            case R.id.addItem :{
                addItemInInventoryList(v);
                break;
            }
        }
    }

    private void addItemInInventoryList(View view)
    {
        if(upcCode.getText().toString().equals("") || quantity.getText().toString().equals(""))
        {
            AppService.notifyUser(view,"UPC / Quantity Required ");
            clearData();
            return;
        }
        introText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        InventoryItem item = new InventoryItem(upcCode.getText().toString(),description.getText().toString(),Integer.parseInt(quantity.getText().toString()));
        updateInventoryList(item);
        adapter.notifyDataSetChanged();
        clearData();
    }



    private void clearData() {
        upcCode.setText("");
        description.setText("");
        quantity.setText("");
        upcCode.requestFocus();
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
        Collections.reverse(inventoryList);
        inventoryList.add(newItem);
        Collections.reverse(inventoryList);

    }


    private void checkUpcInPriceBook(String s) {
        boolean found=false;
        ListIterator<PriceBookEntry> iterator = PriceBookService.getInstance().getPriceBook().listIterator();
        while(iterator.hasNext())
        {
            PriceBookEntry pb =  iterator.next();
            if(pb.getUpc().equals(s.toString()))
            {
                description.setText(pb.getDescription());
                quantity.requestFocus();
                //activity.startListening();
                //SpeechListenerService.start(this,getContext());
                found=true;
                break;
            }
            description.setText("");
        }
        if(!found)
        {
            AppService.notifyUser(this.getView(),"New Item Found !");
        }
    }

    public void setQuantity(String quantityValue)
    {
        quantity.setText(quantityValue);
    }


}
