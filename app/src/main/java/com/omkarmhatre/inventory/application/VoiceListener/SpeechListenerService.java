package com.omkarmhatre.inventory.application.VoiceListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.omkarmhatre.inventory.application.Inventory.InventoryFragment;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechListenerService extends RecognitionService  implements RecognitionListener{

    private static InventoryFragment fragment;
    private static Context context;

    private String LOG_TAG="Voice";

    private SpeechRecognizer speech=null;
    private Intent recognizerIntent;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    int res;

    private TextToSpeechConverter converter;

    public static void start(InventoryFragment fragment, Context context)
    {
        SpeechListenerService.fragment =fragment;
        SpeechListenerService.context=context;
        context.startService(new Intent(context,SpeechListenerService.class));

    }

    @Override
    protected void onStartListening(Intent recognizerIntent, Callback listener) {

    }

    @Override
    protected void onCancel(Callback listener) {

    }

    @Override
    protected void onStopListening(Callback listener) {

    }


    @Override
    public void onCreate() {
        super.onCreate();

        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        speech.startListening(recognizerIntent);
        converter = new TextToSpeechConverter(this);

    }

    @Override
    public void onResults(Bundle results) {
        Log.wtf(LOG_TAG,results.toString());

//        String quantity=TextToNumberConverter.replaceNumbers(results.getString(SpeechRecognizer.RESULTS_RECOGNITION));
//        Toast.makeText(context,quantity,Toast.LENGTH_LONG).show();

        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String quantity = "";
        for (String result : matches)
            if(isNumber(result))
            {
                quantity += result + "";
            }
        //text=TextToNumberConverter.replaceNumbers(text);
        converter.speakSpeech(quantity);
        SpeechListenerService.fragment.setQuantity(quantity);
        stopService(new Intent(this, SpeechListenerService.class));
    }


    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

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
        stopService(new Intent(this,SpeechListenerService.class));
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

    private boolean isNumber(String word)
    {
        boolean isNumber = false;
        try
        {
            Integer.parseInt(word);
            isNumber = true;
        } catch (NumberFormatException e)
        {
            isNumber = false;
        }
        return isNumber;
    }

}
