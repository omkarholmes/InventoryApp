package com.omkarmhatre.inventory.application.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.omkarmhatre.inventory.application.R;

import static android.content.Context.VIBRATOR_SERVICE;

public final class AppService {

    public static final int ITEM_FOUND=1;
    public static final int ITEM_NOT_FOUND=2;

    public static void notifyUser(View view, String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void  notifyUser(Context context, int option)
    {
        Vibrator myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        switch(option)
        {
            case 1:{
                MediaPlayer mp = MediaPlayer.create(context, R.raw.item_found);
                myVib.vibrate(100);
                mp.start();
                break;
            }
            case 2:{
                MediaPlayer mp = MediaPlayer.create(context, R.raw.item_not_found);
                myVib.vibrate(150);
                mp.start();
                break;
            }
        }

    }

}
