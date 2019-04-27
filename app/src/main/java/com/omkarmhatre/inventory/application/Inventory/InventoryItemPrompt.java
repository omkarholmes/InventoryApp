package com.omkarmhatre.inventory.application.Inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.InventoryService;

import java.util.List;

public class InventoryItemPrompt implements View.OnClickListener {

    private Activity activity;
    private int position;
    private boolean editOn;
    private List<InventoryItem> inventoryItemList = InventoryService.getInstance().getInventoryList();

    private View dialogView;
    private TextView tvUpc;
    private EditText etUpc;
    private TextView tvDescription;
    private EditText etDescription;
    private EditText etPrevQuantity;
    private TextView tvPrevQuantity;
    private EditText etQuantity;
    private TextView tvQuantity;
    private TextView tvTotalQuantity;
    private EditText etTotalQuantity;


    private Button delete;
    private Button edit;
    private Button clear;
    private Button previous;
    private Button next;
    private Button save;

    public void showDetails(Activity activity, int position) {

        this.position=position;
        this.activity=activity;

        createAlert(activity);

    }

    private void createAlert(Activity activity) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_inventory_entry_details, null);
        initialiseViewFields();
        dialogBuilder.setView(dialogView);

        setValues(position);

        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.height= DrawerLayout.LayoutParams.WRAP_CONTENT;
        params.width= DrawerLayout.LayoutParams.MATCH_PARENT;
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setAttributes(params);
        alertDialog.show();
    }


    private void setValues(int position)
    {
        this.position=position;
        InventoryItem inventoryItem = inventoryItemList.get(position);
        tvUpc.setText(inventoryItem.getUpc());
        tvDescription.setText(inventoryItem.getDescription());
        tvQuantity.setText(""+(inventoryItem.getQuantity()-inventoryItem.getLastQuantity()));
        tvPrevQuantity.setText(""+inventoryItem.getLastQuantity());
        tvTotalQuantity.setText(""+inventoryItem.getQuantity());

    }

    private void initialiseViewFields() {
        tvUpc = dialogView.findViewById(R.id.tvUpc);
        etUpc = dialogView.findViewById(R.id.etUpc);

        tvDescription = dialogView.findViewById(R.id.tvDescription);
        etDescription = dialogView.findViewById(R.id.etDescription);

        etPrevQuantity = dialogView.findViewById(R.id.etPreviousQuantity);
        tvPrevQuantity = dialogView.findViewById(R.id.tvPreviousQuantity);

        etQuantity = dialogView.findViewById(R.id.etQuantity);
        tvQuantity = dialogView.findViewById(R.id.tvQuantity);

        tvTotalQuantity = dialogView.findViewById(R.id.tvTotalQuantity);
        etTotalQuantity = dialogView.findViewById(R.id.etTotalQuantity);

        editPrompt(false);

        delete = dialogView.findViewById(R.id.deleteBtn);
        edit = dialogView.findViewById(R.id.editBtn);
        clear = dialogView.findViewById(R.id.clearBtn);
        previous = dialogView.findViewById(R.id.prevBtn);
        next = dialogView.findViewById(R.id.nextBtn);
        save = dialogView.findViewById(R.id.saveBtn);


        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
        previous.setOnClickListener(this);
        clear.setOnClickListener(this);
        next.setOnClickListener(this);
        save.setOnClickListener(this);


    }

    private void editPrompt(boolean edit) {

        this.editOn=edit;
        if(edit)
        {
            hideTextBox();
            showEditBox();
        }
        else {
            hideEditBox();
            showText();
        }

    }

    private void hideTextBox() {
        tvDescription.setVisibility(View.GONE);
        tvPrevQuantity.setVisibility(View.GONE);
        tvQuantity.setVisibility(View.GONE);
        tvTotalQuantity.setVisibility(View.GONE);
        tvUpc.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        this.edit.setVisibility(View.GONE);
    }

    private void showEditBox() {
        etDescription.setVisibility(View.VISIBLE);
        etPrevQuantity.setVisibility(View.VISIBLE);
        etQuantity.setVisibility(View.VISIBLE);
        etTotalQuantity.setVisibility(View.VISIBLE);
        etUpc.setVisibility(View.VISIBLE);
        clear.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
    }

    private void showText() {
        tvDescription.setVisibility(View.VISIBLE);
        tvPrevQuantity.setVisibility(View.VISIBLE);
        tvQuantity.setVisibility(View.VISIBLE);
        tvTotalQuantity.setVisibility(View.VISIBLE);
        tvUpc.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        this.edit.setVisibility(View.VISIBLE);

    }

    private void hideEditBox() {
        etDescription.setVisibility(View.GONE);
        etPrevQuantity.setVisibility(View.GONE);
        etQuantity.setVisibility(View.GONE);
        etTotalQuantity.setVisibility(View.GONE);
        etUpc.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.deleteBtn:{
                break;
            }
            case R.id.editBtn:{
                break;
            }
            case R.id.clearBtn:{
                break;
            }
            case R.id.saveBtn:{
                break;
            }
            case R.id.prevBtn:{
                if(position>0)
                {
                    position--;
                    setValues(position);
                    previous.setTextColor(activity.getResources().getColor(R.color.colorAccent));
                }else {
                    previous.setTextColor(Color.parseColor("#0F0F0F"));
                }
                break;
            }
            case R.id.nextBtn:{
                if(position<inventoryItemList.size())
                {
                    position++;
                    setValues(position);
                    next.setTextColor(activity.getResources().getColor(R.color.colorAccent));
                }
                else {
                    next.setTextColor(Color.parseColor("#0F0F0F"));
                }
                break;
            }

        }

    }
}
