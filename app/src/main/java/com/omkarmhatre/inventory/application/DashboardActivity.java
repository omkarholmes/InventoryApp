package com.omkarmhatre.inventory.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import com.omkarmhatre.inventory.application.Inventory.InventoryFragment;
import com.omkarmhatre.inventory.application.Inventory.InventoryManualInputFragment;
import com.omkarmhatre.inventory.application.PriceBook.PriceBookFragment;
import com.omkarmhatre.inventory.application.Utils.PriceBookService;
import com.omkarmhatre.inventory.application.Utils.TextToNumberConverter;

import java.util.ArrayList;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener,RecognitionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private static final int REQUEST_RECORD_PERMISSION = 100;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 110;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 120;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 130;
    private String LOG_TAG="Voice";

    private SpeechRecognizer speech=null;
    private Intent recognizerIntent;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        //fab.setVisibility(View.GONE);

        //init Speech Recogniser
        initialiseSpeechRecognition();

        //Permissions
        makePermisionRequest();

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        setUpViewPager();

    }

    private void setUpViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int pageNumber, float v, int i1) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int pageNumber) {
                fab.setVisibility(View.VISIBLE);
                switch(pageNumber)
                {
                    case 0:{
                        fab.setImageDrawable(getDrawable(R.drawable.add));
                        break;
                    }
                    case 1 :
                    {
                        ((InventoryFragment)mSectionsPagerAdapter.getItem(1)).refreshView();
                        fab.setImageDrawable(getDrawable(R.drawable.keypad_icon));
                        break;
                    }
                    case 2:
                    {
                        fab.setVisibility(View.GONE);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int pageNumber) {

            }
        });
    }

    private void initialiseSpeechRecognition() {

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

    }


    /** To request User Permissions */

    private void makePermisionRequest() {
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        //askForPermission(Manifest.permission.BLUETOOTH,REQUEST_BLUETOOTH_PERMISSION);
        askForPermission(Manifest.permission.RECORD_AUDIO,REQUEST_RECORD_PERMISSION);

    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(DashboardActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{permission}, requestCode);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Start Listening
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
                break;
            }
            case REQUEST_READ_EXTERNAL_STORAGE_PERMISSION:{
                if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    // Create the adapter that will return a fragment for each of the three
                    // primary sections of the activity.
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    // Set up the ViewPager with the sections adapter.
                    mViewPager = findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                }
                break;
            }
            case REQUEST_BLUETOOTH_PERMISSION: {
                break;
            }
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onPostResume() {

        switch (mViewPager.getCurrentItem())
        {
            case 2:{
                fab.setVisibility(View.GONE);
                break;
            }
            default:{
                fab.setVisibility(View.VISIBLE);
            }

        }
        super.onPostResume();
    }

    /**  Speech Recognition Methods  */

   public void startListening()
   {
       speech.startListening(recognizerIntent);
   }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
        if(errorMessage.equalsIgnoreCase("No speech input"))
        {
            Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG,results.toString());
        ArrayList<String> matches=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text ="";
        for (String result : matches)
            text += result + " ";
        String quantity=TextToNumberConverter.replaceNumbers(text);
        Toast.makeText(this,quantity,Toast.LENGTH_LONG).show();
        ((InventoryFragment)mSectionsPagerAdapter.getItem(1)).setQuantity(quantity);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    /**     OnClick     */

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.fab : {
                switch (mViewPager.getCurrentItem())
                {
                    case 0 :{
                        PriceBookFragment fragment = (PriceBookFragment) mSectionsPagerAdapter.getItem(0);
                        PriceBookService.getInstance().showDialog();;
                        break;
                    }
                    case 1:{
                        fab.setVisibility(View.GONE);
                        InventoryFragment fragment =(InventoryFragment) mSectionsPagerAdapter.getItem(1);
                        fragment.showKeyPad();
                        break;
                    }
                }
            }
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment= null;
            switch (position)
            {
                case 0 :
                {
                    fragment= PriceBookFragment.newInstance();
                    break;
                }
                case 1 :
                {
                    fragment= InventoryFragment.newInstance(DashboardActivity.this);
                    break;
                }
                case 2:
                {
                    fragment= InventoryManualInputFragment.getInstance(DashboardActivity.this);
                    break;
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


}
