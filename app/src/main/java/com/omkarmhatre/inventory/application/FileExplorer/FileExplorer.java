package com.omkarmhatre.inventory.application.FileExplorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.omkarmhatre.inventory.application.DashboardActivity;
import com.omkarmhatre.inventory.application.Inventory.InventoryItem;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.InventoryService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileExplorer {

    private File path = new File(Environment.getExternalStorageDirectory() + "/Inventory Files");
    private File priceBookFolder = new File(path,"/Price Books");
    private File inventoryListsFolder = new File(path,"/Inventory Lists");
    private File imports = new File(path,"ImportedPriceBook.csv");
    private File inventoryFile =new File(inventoryListsFolder, "InventoryList.csv");
    private static final String CSV_SEPARATOR = ",";
    public List<PriceBookEntry> priceBook;

    private static final String TAG = "F_PATH";
    // Check if the first level of the directory structure is the one showing
    private Boolean firstLvl = true;
    private List<DirectoryItem> contents = new ArrayList<>();
    private File storage = new File(Environment.getExternalStorageDirectory() + "");
    private String chosenFile;

    public List<String> importedPriceBooks = new ArrayList<>();

    private DirectoryItemAdapter adapter;
    private Activity activity;
    private boolean isPresent;


    public FileExplorer(Activity activity) {
        this.activity=activity;
        loadFileList(storage);
    }

    public FileExplorer ()
    {
        writeIntoFile();
    }

    public void loadFileList(File path) {
        try {
                path.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

        // Checks whether path exists
        if (path.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    // Filters based on whether the file is hidden or not
                    return (sel.isFile() || sel.isDirectory())
                            && !sel.isHidden();

                }
            };

            String[] fList = path.list(filter);
            /*fileList = new Item[fList.length];*/
            for (int i = 0; i < fList.length; i++) {

                contents.add(new DirectoryItem(fList[i], R.drawable.file_icon)) ;

                // Convert into file path
                File sel = new File(path, fList[i]);

                // Set drawables
                if (sel.isDirectory()) {
                    contents.get(i).icon = R.drawable.directory_icon;
                    Log.d("DIRECTORY", contents.get(i).file);
                } else {
                    Log.d("FILE", contents.get(i).file);
                }
            }

            if (!firstLvl) {
                /*DirectoryItem temp[] = new DirectoryItem[contents.size()+1];
                for (int i = 0; i < contents.size(); i++) {
                    temp[i + 1] = contents.get(i);
                }
                temp[0] = new DirectoryItem("Up", R.drawable.directory_up);
                fileList = temp;*/
                Collections.reverse(contents);
                contents.add(new DirectoryItem("Up", R.drawable.directory_up));
                Collections.reverse(contents);
            }
        }
        else {
            Log.e(TAG, "path does not exist");
        }

        adapter = new DirectoryItemAdapter(activity,contents);


    }

    public void explore(List<DirectoryItem> contents) {
            List<DirectoryItem> dirList = new ArrayList<>();
            dirList=contents;
            /*final List<DirectoryItem> contents=fileExplorer.getContents();*/
            if (contents.isEmpty()) {
                Log.e(TAG, "No files loaded");
            }
    }

    private void writeIntoFile()
    {
        try {
            path.mkdirs();
            priceBookFolder.mkdir();
            inventoryListsFolder.mkdir();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

    }

    public List<PriceBookEntry> readCSVFile(File file) throws IOException {


        updatePriceBooks(file);
        //AppService.notifyUser(view,file.getAbsolutePath());
        InputStream stream = new FileInputStream(file);
        //AppService.notifyUser(view,stream.toString());
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        String line="";
        priceBook= PriceBookService.getInstance().getPriceBook();
        priceBook.clear();
        while ((line=csvReader.readLine()) != null)
        {
            //Splitting the line by ','
            String[] tokens =line.split(",");
            if(tokens.length >=2)
            {
                PriceBookEntry newEntry = new PriceBookEntry(tokens[0],tokens[1]);
                priceBook.add(newEntry);
            }

        }

        return priceBook;

    }

    private void updatePriceBooks(File file) throws IOException {

        importedPriceBooks.add(getFileName(file));
        if(imports.exists()) {
            InputStream stream = new FileInputStream(imports);
            //AppService.notifyUser(view,stream.toString());
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = csvReader.readLine()) != null) {
                List<String> importedBooks = new ArrayList<>();
                //Splitting the line by ','
                String[] tokens = line.split(",");
                if (tokens.length >= 0 && tokens[0].equals(file.getName())) {
                    isPresent = true;
                    break;
                }

            }
            csvReader.close();
        }
        if(!isPresent)
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(imports, true), "UTF-8"));
            StringBuffer oneLine = new StringBuffer();
            oneLine.append(getFileName(file));
            oneLine.append(CSV_SEPARATOR);
            /*if(moveFile(file,priceBookFolder))
            {
                oneLine.append(newDest.toString());
            }*/
            oneLine.append(file.toString());
            bw.write(oneLine.toString());
            bw.newLine();
            bw.flush();
            bw.close();
        }


    }

    private boolean moveFile(File source, File destination) {
        File targetDestination =new File(destination,getFileName(source));
        return source.renameTo(targetDestination);
    }

    private  String getFileName(File file)
    {
        String filePath [] = file.toString().split("/");
        int index = filePath.length;

        return filePath[index-1];
    }

    public void writeToCSV(List<InventoryItem> productList) {
        try {
            //writeIntoFile();
            if (path.exists()) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inventoryFile, false), "UTF-8"));
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

    }

    public List<DirectoryItem> getContents() {
        return contents;
    }

    //    private void createFile()
//    {
//
//        File root= new File(Environment.getExternalStorageDirectory(),"InVoice");
//        if(!root.exists())
//        {
//            root.mkdirs();
//            try {
//                root.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            base=root;
//        }
//        else
//        {
//            //Toast.makeText(this, "File Not Created .", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void writeFile()
//    {
//
//        File f= new File(base,fileNmame);
//
//        if(f.exists()) {
//            FileWriter writer;
//            try {
//                writer = new FileWriter(f);
//                writer.append("");
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String t= "File Write Complete.";
//
//        }
//        readFile();
//    }
//
//    public void readFile()
//    {
//        StringBuilder text = new StringBuilder();
//        File root= new File(Environment.getExternalStorageDirectory(),"InVoice");
//        if(root.exists())
//        {
//            //Get the text file
//            File file = new File(root,fileNmame);
//            //Read text from file
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(file));
//                String line;
//
//                while ((line = br.readLine()) != null) {
//                    text.append(line);
//                    text.append('\n');
//                }
//                br.close();
//            }
//            catch (IOException e) {
//                //You'll need to add proper error handling here
//            }
//
//        }
//        else
//        {
//            createFile();
//            //readFile();
//        }
//
//    }

}
