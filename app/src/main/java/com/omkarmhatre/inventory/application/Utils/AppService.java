package com.omkarmhatre.inventory.application.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public final class AppService {

    public static void notifyUser(View view, String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
