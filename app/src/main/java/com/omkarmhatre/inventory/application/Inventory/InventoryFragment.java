package com.omkarmhatre.inventory.application.Inventory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.DashboardActivity;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.CustomUI;
import com.omkarmhatre.inventory.application.Utils.InventoryService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;
import com.omkarmhatre.inventory.application.VoiceListener.SpeechListenerService;
import com.omkarmhatre.inventory.application.VoiceListener.TextToSpeechConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Inventory fragment containing a simple view.
 */
@SuppressLint("ValidFragment")
public class InventoryFragment extends Fragment implements View.OnClickListener,View.OnTouchListener,Inventory{
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
    @BindView(R.id.keypadView) KeyboardView keypadView;
    @BindView(R.id.keyPadLayout) RelativeLayout keypadLayout;
    @BindView(R.id.closeKeyPad)FloatingActionButton closeKeyPad;

    private  static InventoryFragment fragment;
    List<InventoryItem> inventoryList = InventoryService.getInstance().getInventoryList();
    InventoryItemAdapter adapter;
    DashboardActivity activity;
    Keyboard keypad;
    boolean inputViaVoice=true;
    TextToSpeechConverter converter;


    public InventoryFragment(DashboardActivity activity) {
        this.activity=activity;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(DashboardActivity activity) {
         if(fragment == null) {
             fragment=new InventoryFragment(activity);
         }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
        ButterKnife.bind(this,rootView);
        setupRecyclerView(new LinearLayoutManager(container.getContext()));
        setOnClickListeners();
        setOnTouchListeners();
        setupTextWatcher();
        setUpKeyPad();

        createTextToSpeechConverter();
        //InventoryService.getInstance().writeIntoFile();
        return rootView;
    }

    private void setUpKeyPad() {
        keypadView=CustomUI.setupKeypadView(keypadView,getActivity(),this);
    }

    private void setOnTouchListeners() {
        upcCode.setOnTouchListener(this);
        quantity.setOnTouchListener(this);
        upcCode.requestFocus();
    }

    private void createTextToSpeechConverter() {
        converter = new TextToSpeechConverter(getContext());
    }


    /**
     * Setting Up the Custom Keyboard View
     */

    @Override
    public void onPause() {
        super.onPause();
        closeKeyPad();
        InventoryService.getInstance().writeToCSV(inventoryList);
        //keypadLayout.setVisibility(View.GONE);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        //closeKeyPad();
        //SpeechListenerService.start(this,getContext());
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    public void showKeyPad()
    {
        quantity.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.voiceInputOff));
        inputViaVoice=false;
        keypadLayout.setVisibility(View.VISIBLE);
        description.setVisibility(View.GONE);
        activity.fab.setVisibility(View.GONE);
        closeKeyPad.setVisibility(View.VISIBLE);
    }

    private void setOnClickListeners() {
        closeKeyPad.setOnClickListener(this);
        addItem.setOnClickListener(this);
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
                if(!inputViaVoice)
                {
                    return;
                }
                if(s.toString().equals("") && s.toString().length()<=12)
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
                if(!inputViaVoice)
                {
                    return;
                }
                if(s.toString().equals(""))
                {
                    //converter.textInputFound(fragment);
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
                activity.startListening();
                break;
            }
            case R.id.addItem :{
                addItemInInventoryList(v);
                break;
            }
            case R.id.closeKeyPad: {
                closeKeyPad();
                adapter.notifyDataSetChanged();
                break;
            }
            default:{
                InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void closeKeyPad() {
        /*if (this.isResumed() && getActivity().getCurrentFocus().getId() == quantity.getId() )
        {
            quantity.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.voiceInputOn));
            SpeechListenerService.start(this,getContext());
        }*/
        inputViaVoice=true;
        keypadLayout.setVisibility(View.GONE);
        activity.fab.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        closeKeyPad.setVisibility(View.GONE);
    }

//    public void addItemInInventoryList(View view)
//    {
//
//    }

    public void refreshView()
    {
        adapter.notifyDataSetChanged();
    }

    public void clearData() {
        upcCode.setText("");
        description.setText("");
        quantity.setText("");
        upcCode.requestFocus();
    }

//    public void updateInventoryList(InventoryItem newItem) {
//
//    }

    @Override
    public void updateInventoryList(InventoryItem newItem) {
        ListIterator<InventoryItem> iterator = inventoryList.listIterator();
        while(iterator.hasNext())
        {
            InventoryItem item = iterator.next();
            if(item.getUpc().equals(newItem.getUpc()))
            {
                newItem.setLastQuantity(item.getQuantity());
                newItem.setQuantity(newItem.getQuantity()+item.getQuantity());
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
    }

    @Override
    public void checkUpcInPriceBook(String upc) {
        InventoryItem item = PriceBookService.getInstance().checkInPriceBook(upc);
        description.setText("N/A");
        if(item != null)
        {
            description.setText(item.getDescription());
            if(inputViaVoice)
            {
                SpeechListenerService.start(this,getContext());
            }
        }
        else
        {
            AppService.notifyUser(getContext(),AppService.ITEM_NOT_FOUND);
        }
        quantity.requestFocus();
        createTextToSpeechConverter();
    }

    @Override
    public void addItemInInventoryList(View view) {
        if(upcCode.getText().toString().equals("") || quantity.getText().toString().equals(""))
        {
            if(upcCode.getText().toString().equals(""))
            {
                AppService.notifyUser(view,"UPC / Quantity Required ");
                clearData();
            }
            if(quantity.getText().toString().equals(""))
            {
                AppService.notifyUser(view,"Quantity Required ");
                quantity.setText("");
                quantity.requestFocus();
            }
            return;
        }
        introText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        InventoryItem item = new InventoryItem(upcCode.getText().toString(),description.getText().toString(),Integer.parseInt(quantity.getText().toString()));
        updateInventoryList(item);
        adapter.notifyDataSetChanged();
        clearData();
    }


//    public void checkUpcInPriceBook(String s) {
//
//    }

    public void setQuantity(String quantityValue)
    {
        if(quantityValue.trim().equals(""))
        {
            converter.textInputFound(fragment);
        }
        else
        {
            quantity.setText(quantityValue);
        }
        createTextToSpeechConverter();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ((EditText)v).requestFocus();
        InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        showKeyPad();
        return true;
    }
}
