package com.omkarmhatre.inventory.application.Utils;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;

import com.omkarmhatre.inventory.application.Inventory.Inventory;
import com.omkarmhatre.inventory.application.R;

public class CustomUI {

    private static Keyboard keypad;

    public static KeyboardView setupKeypadView(final KeyboardView keypadView, final Activity activity, final Inventory inventory) {
        // Create the Keyboard
        keypad= new Keyboard(activity,R.xml.numeric_keypad);

        // Attach the keyboard to the view
        keypadView.setKeyboard( keypad );

        // Do not show the preview balloons
        keypadView.setPreviewEnabled(false);

        // Install the key handler
        keypadView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            //Initialising the keyboard Listener
            @Override public void onKey(int primaryCode, int[] keyCodes)
            {
                //Here check the primaryCode to see which key is pressed
                //based on the android:codes property
                View v;
                v = activity.getCurrentFocus();
                String result;
                result=((EditText)v).getText().toString();
                switch (primaryCode)
                {
                    case -1:{
                        if(result.length() <=1)
                        {
                            result="";
                        }
                        else
                        {
                            result=result.substring(0,result.length()-1);
                        }
                        ((EditText)v).setText(result);
                        break;
                    }
                    case 100: {

                        if(v.getId() == R.id.quantity || v.getId() == R.id.currentQuantityEntry)
                        {

                            inventory.addItemInInventoryList(keypadView);
                            result="";
                        }
                        else
                        {
                            inventory.checkUpcInPriceBook(result);
                        }

                        break;
                    }
                    default:{
                        result+=primaryCode;
                        ((EditText)v).setText(result);
                    }
                }
                if(result.length()>13)
                {
                    return;
                }
                ((EditText)v).setSelection(result.length());


            }

            @Override public void onPress(int arg0) {
            }

            @Override public void onRelease(int primaryCode) {
            }

            @Override public void onText(CharSequence text) {
            }

            @Override public void swipeDown() {
            }

            @Override public void swipeLeft() {
            }

            @Override public void swipeRight() {
            }

            @Override public void swipeUp() {
            }
        });

        return keypadView;
    }
}
