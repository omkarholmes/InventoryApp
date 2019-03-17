package com.omkarmhatre.inventory.application.Utils;

import android.os.Environment;
import android.util.Log;

import com.omkarmhatre.inventory.application.Inventory.InventoryItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class InventoryService {

    private File path = new File(Environment.getExternalStorageDirectory() + "/Inventory Files");

    private static final String TAG = "F_PATH";
    private static InventoryService instance;
    //European countries use ";" as
    //CSV separator because "," is their digit separator
    private static final String CSV_SEPARATOR = ",";

    public static InventoryService getInstance()
    {
        if(instance == null)
        {
            instance = new InventoryService();
        }
        return instance;
    }

    public void writeIntoFile()
    {
        try {
            path.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

    }

    private static void writeToCSV(ArrayList<InventoryItem> productList)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("products.csv"), "UTF-8"));
            for (InventoryItem product : productList)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(product.getUpc());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(product.getDescription().trim());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(product.getQuantity());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(product.getLastQuantity());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {}
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }


}
