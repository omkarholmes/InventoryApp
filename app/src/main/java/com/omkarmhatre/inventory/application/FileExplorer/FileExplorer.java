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
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileExplorer {

    public List<PriceBookEntry> priceBook;

    private static final String TAG = "F_PATH";
    // Check if the first level of the directory structure is the one showing
    private Boolean firstLvl = true;
    private List<DirectoryItem> contents = new ArrayList<>();
    private File path = new File(Environment.getExternalStorageDirectory() + "");
    private String chosenFile;

    private DirectoryItemAdapter adapter;
    private Activity activity;


    public FileExplorer(Activity activity) {
        this.activity=activity;
        loadFileList(path);
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

    public List<PriceBookEntry> readCSVFile(File file) throws IOException {

        //AppService.notifyUser(view,file.getAbsolutePath());
        InputStream stream = new FileInputStream(file);
        //AppService.notifyUser(view,stream.toString());
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        String line="";
        priceBook= PriceBookService.getInstance().getPriceBook();
        priceBook.clear();
        while ((line=csvReader.readLine()) != null)
        {
            //Spliting the line by ','
            String[] tokens =line.split(",");
            if(tokens.length >=2)
            {
                PriceBookEntry newEntry = new PriceBookEntry(tokens[0],tokens[1]);
                priceBook.add(newEntry);
            }

        }

        return priceBook;

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
