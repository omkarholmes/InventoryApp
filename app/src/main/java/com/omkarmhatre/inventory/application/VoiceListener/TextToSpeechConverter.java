package com.omkarmhatre.inventory.application.VoiceListener;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.omkarmhatre.inventory.application.Inventory.InventoryFragment;

import java.util.Locale;

public class TextToSpeechConverter {

    Context context;
    TextToSpeech converter;
    int ttsSupported;
    int flag=1;
    String textInputFound="Please Input Numbers Only !!";
    Fragment fragment;

    public TextToSpeechConverter(final Context context) {

        this.context=context;
        converter = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    converter.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {

                        }

                        @Override
                        public void onDone(String utteranceId) {
                            if(utteranceId.equals(textInputFound)) {
                                SpeechListenerService.start((InventoryFragment) fragment,context);
                            }
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                    ttsSupported=converter.setLanguage(Locale.getDefault());
                }
                else
                {
                    //Toast.makeText(context,"Feature not supported.",Toast.LENGTH_SHORT);
                }
            }
        });

    }

    //      Text To Speech Converter  ............

    public void speakSpeech(String textToSpeak) {

        if(flag==1)
        {
            if((ttsSupported == TextToSpeech.LANG_MISSING_DATA) || (ttsSupported == TextToSpeech.LANG_NOT_SUPPORTED))
            {
                //Toast.makeText(context,"Feature not supported.",Toast.LENGTH_SHORT);
            }
            else
            {
                flag=0;
                converter.speak(textToSpeak,TextToSpeech.QUEUE_FLUSH,null,textToSpeak);
                //converter.shutdown();
            }
        }
        else
        {
            if (converter!=null)
            {
                converter.stop();
                flag=1;
            }
        }
        //converter.shutdown();

    }

    public void textInputFound(Fragment fragment)
    {
     this.fragment=fragment;
     speakSpeech(textInputFound);
    }


}
