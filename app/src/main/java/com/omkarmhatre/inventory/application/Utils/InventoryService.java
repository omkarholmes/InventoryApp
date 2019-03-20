package com.omkarmhatre.inventory.application.Utils;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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
import java.util.List;

public class InventoryService {

    private File path = new File(Environment.getExternalStorageDirectory() + "/Inventory Files");

    private static final String TAG = "F_PATH";
    private static InventoryService instance;
    //European countries use ";" as
    //CSV separator because "," is their digit separator
    private static final String CSV_SEPARATOR = ",";


    String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    String fileName = "AnalysisData.csv";
    String filePath = baseDir + File.separator + fileName;
    File f = new File(filePath);
    CSVWriter writer;
    FileWriter mFileWriter;

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

    public void writeToCSV(List<InventoryItem> productList) {
        try {
            //path.mkdir();
            if (path.exists()) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path, "products.csv"),false), "UTF-8"));
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

        /*// File exist
        try
        {


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








}
