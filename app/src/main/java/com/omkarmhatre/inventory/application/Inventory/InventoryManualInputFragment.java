package com.omkarmhatre.inventory.application.Inventory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.DashboardActivity;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.CustomUI;
import com.omkarmhatre.inventory.application.Utils.InventoryService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class InventoryManualInputFragment extends Fragment implements View.OnClickListener,View.OnTouchListener,Inventory {

    @BindView(R.id.keypadView) KeyboardView keypadView;
    @BindView(R.id.upcEntry) EditText upc;
    @BindView(R.id.descriptionEntry) EditText description;
    @BindView(R.id.clearFunction) LinearLayout clearBtn;
    @BindView(R.id.deleteFunction) LinearLayout deleteBtn;
    @BindView(R.id.currentQuantityEntry) EditText currentQuantity;
    @BindView(R.id.prevQuantityEntry) TextView previousQuantity;
    @BindView(R.id.totalQuantityEntry) TextView totalQuantity;

    private static InventoryManualInputFragment instance;
    DashboardActivity activity;
    List<InventoryItem> inventoryList =InventoryService.getInstance().getInventoryList();

    @SuppressLint("ValidFragment")
    private InventoryManualInputFragment(DashboardActivity activity)
    {
        this.activity=activity;
    }

    public static InventoryManualInputFragment getInstance(DashboardActivity activity)
    {
        if(instance == null)
        {
            instance = new InventoryManualInputFragment(activity);
        }

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory_keyboard, container, false);
        ButterKnife.bind(this,rootView);
        setOnTouchListeners();
        setUpKeyPad();
        setTextWatcher();
        setOnClickListeners();
        //keypadView=CustomUI.setupKeypadView(keypadView,getActivity());
        return rootView;
    }

    private void setOnClickListeners() {
        clearBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    private void setTextWatcher() {
        currentQuantity.addTextChangedListener(new TextWatcher() {
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
                    totalQuantity.setText("");
                }
                else
                {
                    String total =""+(Integer.parseInt(s.toString())+Integer.parseInt(previousQuantity.getText().toString().equals("") ? ""+0 : previousQuantity.getText().toString()));
                    totalQuantity.setText(total);
                }

            }
        });
    }

    public void setUpKeyPad() {
        keypadView=CustomUI.setupKeypadView(keypadView,getActivity(),this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        //activity.fab.setVisibility(View.GONE);
        //setUpKeyPad();
        super.onResume();
    }

    private void setOnTouchListeners() {
        upc.setOnTouchListener(this);
        //description.setOnTouchListener(this);
        currentQuantity.setOnTouchListener(this);
        upc.requestFocus();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ((EditText)v).requestFocus();
        InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        return true;
    }

    @Override
    public void updateInventoryList(InventoryItem newItem) {
       // InventoryService.getInstance().updateInventoryList(newItem,getContext());
        ListIterator<InventoryItem> iterator = inventoryList.listIterator();
        while(iterator.hasNext())
        {
            InventoryItem item = iterator.next();
            if(item.getUpc().equals(newItem.getUpc()))
            {
                newItem.setLastQuantity(item.getQuantity());
                newItem.setQuantity(newItem.getQuantity()+item.getQuantity());
                //previousQuantity.setText(newItem.getLastQuantity());
                AppService.notifyUser(getContext(),AppService.ITEM_FOUND);
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
        InventoryService.getInstance().writeToCSV(inventoryList);
        clearData();
    }

    @Override
    public void checkUpcInPriceBook(String upc) {

        InventoryItem item ;

        item=InventoryService.getInstance().checkInInventoryList(upc);
        if(item==null)
        {
            item= PriceBookService.getInstance().checkInPriceBook(upc);
        }

        description.setText("N/A");
        previousQuantity.setText("0");
        if(item != null)
        {
            description.setText(item.getDescription());
            String current=""+item.getQuantity();
            previousQuantity.setText(current);
        }
        else
        {
            AppService.notifyUser(getContext(),AppService.ITEM_NOT_FOUND);
        }
        currentQuantity.requestFocus();
    }

    @Override
    public void addItemInInventoryList(View view) {
        if(upc.getText().toString().equals("") || currentQuantity.getText().toString().equals(""))
        {
            if(upc.getText().toString().equals(""))
            {
                AppService.notifyUser(view,"UPC / Quantity Required ");
                clearData();
            }
            if(currentQuantity.getText().toString().equals(""))
            {
                AppService.notifyUser(view,"Quantity Required ");
                currentQuantity.setText("");
                currentQuantity.requestFocus();
            }
            return;
        }
        InventoryItem item = new InventoryItem(upc.getText().toString(),description.getText().toString(),Integer.parseInt(currentQuantity.getText().toString()));
        updateInventoryList(item);
        //make a call to fragment with list view
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void clearData() {
        upc.setText("");
        description.setText("");
        currentQuantity.setText("");
        previousQuantity.setText("");
        upc.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.clearFunction:
            {
                clearData();
                break;
            }
            case R.id.deleteFunction:
            {
                break;
            }
        }
    }
}
