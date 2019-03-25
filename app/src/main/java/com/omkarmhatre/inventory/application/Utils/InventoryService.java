package com.omkarmhatre.inventory.application.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.omkarmhatre.inventory.application.Inventory.Inventory;
import com.omkarmhatre.inventory.application.Inventory.InventoryItem;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class InventoryService {

    private File path = new File(Environment.getExternalStorageDirectory() + "/Inventory Files");
    private File inventoryFile =new File(path, "InventoryList.csv");
    private static final String TAG = "F_PATH";
    private static InventoryService instance;
    private static final String CSV_SEPARATOR = ",";


    String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String fileName = "AnalysisData.csv";

    private  List<InventoryItem> inventoryList = new ArrayList<>();


    public static InventoryService getInstance()
    {
        if(instance == null)
        {
            instance = new InventoryService();
        }
        return instance;
    }

    public void setInventoryList(List<InventoryItem> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<InventoryItem> getInventoryList() {
        return inventoryList;
    }

    private void writeIntoFile()
    {
        try {
            path.mkdirs();

        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

    }

    public void writeToCSV(List<InventoryItem> productList) {
        try {
            writeIntoFile();
            if (path.exists()) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inventoryFile,false), "UTF-8"));
                for (InventoryItem product : productList) {
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
        } catch (UnsupportedEncodingException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        /*
        // File exist
        try
        {
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;
        FileWriter mFileWriter;
        if(f.exists()&&!f.isDirectory())
        {
            mFileWriter = new FileWriter(filePath, false);
            writer = new CSVWriter(mFileWriter);
        }
        else
        {
            writer = new CSVWriter(new FileWriter(filePath));
        }

        String[] data = {"Ship Name", "Scientist Name", "..."};

        writer.writeNext(data);

        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public InventoryItem checkInInventoryList(String upc)
    {
        ListIterator<InventoryItem> iterator = inventoryList.listIterator();
        while(iterator.hasNext())
        {
            InventoryItem item = iterator.next();
            if(item.getUpc().equals(upc))
            {
                return item;
            }
        }
        return null;
    }

    public InventoryItem updateInventoryList(InventoryItem newItem, Context context) {
        ListIterator<InventoryItem> iterator = inventoryList.listIterator();
        while(iterator.hasNext())
        {
            InventoryItem item = iterator.next();
            if(item.getUpc().equals(newItem.getUpc()))
            {
                newItem.setLastQuantity(item.getQuantity());
                newItem.setQuantity(newItem.getQuantity()+item.getQuantity());
                //previousQuantity.setText(newItem.getLastQuantity());
                AppService.notifyUser(context,AppService.ITEM_FOUND);
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
        return newItem;
    }





}
