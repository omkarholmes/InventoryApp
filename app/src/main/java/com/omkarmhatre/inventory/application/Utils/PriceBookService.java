package com.omkarmhatre.inventory.application.Utils;

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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.FileExplorer.FileExplorer;
import com.omkarmhatre.inventory.application.Inventory.InventoryItem;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookEntry;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookFragment;
import com.omkarmhatre.inventory.application.R;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PriceBookService {

    private static PriceBookService instance;

    private Activity activity;
    private PriceBookFragment fragment;
    private Context context;
    private List<PriceBookEntry> priceBook = new ArrayList<>() ;

    //File Explorer variables

    // Stores names of traversed directories
    ArrayList<String> str = new ArrayList<String>();
    // Check if the first level of the directory structure is the one showing
    private Boolean firstLvl = true;
    private static final String TAG = "F_PATH";
    private Item[] fileList;
    private File path = new File(Environment.getExternalStorageDirectory() + "");
    private String chosenFile;
    ListAdapter fileExploreAdapter;
    private File priceBookPath;

    public static PriceBookService getInstance()
    {
        if(instance == null)
        {
            instance = new PriceBookService();
        }
        return instance;
    }

    private PriceBookService() {

    }

    public void instantiate(Activity activity, PriceBookFragment fragment, Context context)
    {
        this.activity=activity;
        this.fragment=fragment;
        this.context=context;
    }

    public void setPriceBook(List<PriceBookEntry> priceBook) {
        this.priceBook = priceBook;
    }

    public void addPriceBook(List<PriceBookEntry> priceBook)
    {
        if(this.priceBook.isEmpty()) {
            setPriceBook(priceBook);
        }
        else {
            this.priceBook.addAll(priceBook);
        }
    }

    public List<PriceBookEntry> getPriceBook() {
        return priceBook;
    }

    public InventoryItem checkInPriceBook(String upcCode)
    {
        InventoryItem inventoryItem=null;
        for(PriceBookEntry item : priceBook)
        {
            if(item.getUpc().equals(upcCode))
            {
                return new InventoryItem(item.getUpc(),item.getDescription());
            }
        }

        return inventoryItem;
    }


    /**
     * loadFileList
     * Item
     * showDialog
     * methods needs to be shifted to File Explorer
     */
    public void loadFileList() {
        try {
            path.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

        // Checks whether path exists
        if (path.exists())
        {
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
            fileList = new Item[fList.length];
            for (int i = 0; i < fList.length; i++) {
                fileList[i] = new Item(fList[i], R.drawable.file_icon);

                // Convert into file path
                File sel = new File(path, fList[i]);

                // Set drawables
                if (sel.isDirectory()) {
                    fileList[i].icon = R.drawable.directory_icon;
                    Log.d("DIRECTORY", fileList[i].file);
                } else {
                    Log.d("FILE", fileList[i].file);
                }
            }

            if (!firstLvl) {
                Item temp[] = new Item[fileList.length + 1];
                for (int i = 0; i < fileList.length; i++) {
                    temp[i + 1] = fileList[i];
                }
                temp[0] = new Item("Up", R.drawable.directory_up);
                fileList = temp;
            }
        } else {
            Log.e(TAG, "path does not exist");
        }

        fileExploreAdapter = new ArrayAdapter<Item>(context,
                R.layout.layout_directory_item, R.id.dirItemText,
                fileList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // creates view
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.dirItemText);

                // put the image on the text view
                ImageView icon = view.findViewById(R.id.dirItemIcon);
                icon .setImageDrawable(getContext().getDrawable(fileList[position].icon));

                return view;
            }
        };

    }

    private class Item {
        public String file;
        public int icon;

        public Item(String file, Integer icon) {
            this.file = file;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return file;
        }
    }

    public Dialog showDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (fileList == null) {
            Log.e(TAG, "No files loaded");
            dialog = builder.create();
            return dialog;
        }
        int id=0;
        switch (id) {
            default:
                builder.setTitle("Choose your file");
                builder.setAdapter(fileExploreAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenFile = fileList[which].file;
                        File sel = new File(path + "/" + chosenFile);
                        if (sel.isDirectory()) {
                            firstLvl = false;

                            // Adds chosen directory to list
                            str.add(chosenFile);
                            fileList = null;
                            path = new File(sel + "");

                            loadFileList();

                            //removeDialog(DIALOG_LOAD_FILE);
                            showDialog();
                            Log.d(TAG, path.getAbsolutePath());

                        }

                        // Checks if 'up' was clicked
                        else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

                            // present directory removed from list
                            String s = str.remove(str.size() - 1);

                            // path modified to exclude present directory
                            path = new File(path.toString().substring(0,
                                    path.toString().lastIndexOf(s)));
                            fileList = null;

                            // if there are no more directories in the list, then
                            // its the first level
                            if (str.isEmpty()) {
                                firstLvl = true;
                            }
                            loadFileList();

                            //removeDialog(DIALOG_LOAD_FILE);
                            showDialog();
                            Log.d(TAG, path.getAbsolutePath());

                        }
                        // File picked
                        else {
                            // Perform action with file picked

                            FileExplorer fileExplorer = new FileExplorer(activity);

                            // Pass the Selected File To File Explorer
                            //File priceBookFile=fileExplorer.explore();
                            try {
                                //Read File
                                openSelectedFile(fileExplorer,sel);

                            } catch (IOException e) {
                                Log.wtf("omk",e.getMessage());
                                //AppService.notifyUser(v,"Price Book Generation Failed !");
                            }

                        }

                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }

    private void openSelectedFile(FileExplorer fileExplorer, File file) throws IOException {
        PriceBookService.getInstance().addPriceBook(fileExplorer.readCSVFile(file));
        priceBookPath=file;
        if(priceBook.isEmpty())
        {
            //notify user to add items in price book
            AppService.notifyUser(activity.getCurrentFocus(),"Please fill in some data in file");
            return;
        }
        fragment.showPriceBook();
        AppService.notifyUser(activity.getCurrentFocus(),"Price Book Imported successfully.");
    }

}
