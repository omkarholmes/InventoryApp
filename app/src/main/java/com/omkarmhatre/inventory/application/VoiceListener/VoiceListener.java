package com.omkarmhatre.inventory.application.VoiceListener;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceListener extends AppCompatActivity implements RecognitionListener {

    private static final int REQUEST_RECORD_PERMISSION = 100;
    private ProgressBar progressBar;
    private TextView resultantText;
    View  mFloatingWidgetView,floater;
    private Point szWindow = new Point();
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private View removeFloatingWidgetView;
    private ImageView remove_image_view;
    private View listeningView;
    private View listFloater;
    String found;
    private WindowManager mWindowManager;
    int floatX,floatY;
    WindowManager.LayoutParams mParams;
    int t;
    String app ="";
    private static String aname="";

    public void setAname(String aname) {
        this.aname = aname;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        @SuppressLint("WrongViewCast") FrameLayout f = findViewById(R.id.listfloatImg);
//
//        setContentView(f);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        getWindowManagerDefaultDisplay();

//         mParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();
//        mParams.x=floatX;
//        mParams.y=floatY;
//        mWindowManager.updateViewLayout(mFloatingWidgetView, mParams);



        //  progressBar = findViewById(R.id.progressBar);

        //setContentView(listFloater);

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);



        ActivityCompat.requestPermissions
                (VoiceListener.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_PERMISSION);




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(VoiceListener.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }


    private void getWindowManagerDefaultDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            mWindowManager.getDefaultDisplay().getSize(szWindow);
        else {
            int w = mWindowManager.getDefaultDisplay().getWidth();
            int h = mWindowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }
    }



    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        resultantText.setText(text);

        String yn[]= matches.get(0).split("\\s");
        for(String y:yn)
        {
            t=y.toLowerCase().compareToIgnoreCase("yes");
        }
        if(t==0)
        {
            app=aname;
        }


        if (found != null)
        {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(found);
            if (launchIntent != null)
            {
                //startService();
                mWindowManager.removeView(listeningView);
                startActivity(launchIntent);
                //startService(new Intent(this, FloatingWidgetService.class));
                //startService(new Intent(this, SenderActivity.class));
                this.finish();
                //listFloater.setVisibility(View.GONE);
                //                s.sendLog();
            }
            else
            {
                mWindowManager.removeView(listeningView);
                Intent i = new Intent(this, VoiceListener.class);
                startActivity(i);
                this.finish();
            }
        }
        else
        {
            mWindowManager.removeView(listeningView);
            Intent i = new Intent(this, VoiceListener.class);
            startActivity(i);
            this.finish();
        }

    }

    @Override
    public void onPartialResults(Bundle bundle) {
        mWindowManager.removeView(listeningView);
        //startService(new Intent(this, FloatingWidgetService.class));
        this.finish();
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
        }
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
        if(errorMessage.equalsIgnoreCase("No speech input"))
        {
            mWindowManager.removeView(listeningView);
            //startService(new Intent(this, FloatingWidgetService.class));
            this.finish();
        }
    }


    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
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


    public Intent promptSpeech() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something !");

        return i;
    }

}