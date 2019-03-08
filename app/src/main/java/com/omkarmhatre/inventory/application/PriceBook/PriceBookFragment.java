package com.omkarmhatre.inventory.application.PriceBook;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.omkarmhatre.inventory.application.FileExplorer.FileExplorer;
import com.omkarmhatre.inventory.application.FileExplorer.FileExplorerFragment;
import com.omkarmhatre.inventory.application.R;
import com.omkarmhatre.inventory.application.Utils.AppService;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
 public class PriceBookFragment extends Fragment implements View.OnClickListener {

     @BindView(R.id.recyclerView)RecyclerView recyclerView;
     @BindView(R.id.section_label)LinearLayout introText;

     List<PriceBookEntry> priceBook =PriceBookService.getInstance().getPriceBook();
    PriceBookAdapter priceBookListAdapter;

    //File Explorer variables

    // Stores names of traversed directories
    ArrayList<String> str = new ArrayList<String>();
    // Check if the first level of the directory structure is the one showing
    private Boolean firstLvl = true;
    private static final String TAG = "F_PATH";
    private Item[] fileList;
    private File path = new File(Environment.getExternalStorageDirectory() + "");
    private String chosenFile;
    private static final int DIALOG_LOAD_FILE = 1000;
    ListAdapter fileExploreAdapter;



    public PriceBookFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance() {
        PriceBookFragment fragment = new PriceBookFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_price_book, container, false);
        ButterKnife.bind(this,rootView);

        setupRecyclerView(new LinearLayoutManager(container.getContext()));

        /*FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);*/

        loadFileList();
        //showDialog(DIALOG_LOAD_FILE);
        Log.d(TAG, path.getAbsolutePath());
        if(!PriceBookService.getInstance().getPriceBook().isEmpty())
        {
            showPriceBook();
        }

        return rootView;
    }

    //Code to import PriceBook File
    public void showPriceBook() {
        priceBook = PriceBookService.getInstance().getPriceBook();
        priceBookListAdapter.notifyDataSetChanged();
        introText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView(LinearLayoutManager linearLayoutManager) {

        recyclerView.setLayoutManager(linearLayoutManager);
        priceBookListAdapter = new PriceBookAdapter(priceBook);
        recyclerView.setAdapter(priceBookListAdapter);

    }

    private void loadFileList() {
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

        fileExploreAdapter = new ArrayAdapter<Item>(getContext(),
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

    protected Dialog showDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (fileList == null) {
            Log.e(TAG, "No files loaded");
            dialog = builder.create();
            return dialog;
        }

        switch (id) {
            case DIALOG_LOAD_FILE:
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
                            showDialog(DIALOG_LOAD_FILE);
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
                            showDialog(DIALOG_LOAD_FILE);
                            Log.d(TAG, path.getAbsolutePath());

                        }
                        // File picked
                        else {
                            // Perform action with file picked

                            FileExplorer fileExplorer = new FileExplorer(getActivity());

                            // Pass the Selected File To File Explorer
                            //File priceBookFile=fileExplorer.explore();
                            try {
                                //Read File

                                PriceBookService.getInstance().setPriceBook(fileExplorer.readCSVFile(sel));
                                if(priceBook.isEmpty())
                                {
                                    //notify user to add items in price book
                                    AppService.notifyUser(getView(),"Please fill in some data in file");
                                    return;
                                }
                                showPriceBook();
                                AppService.notifyUser(getView(),"Price Book Imported successfully.");
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

    @Override
    public void onClick(View v) {

        showDialog(DIALOG_LOAD_FILE);

        //startActivity(new Intent(getContext(),BluetoothConnector.class));
    }
}