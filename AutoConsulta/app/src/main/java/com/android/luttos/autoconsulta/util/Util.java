package com.android.luttos.autoconsulta.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Util {
    public static void hideSoftKeyboard(Activity activity) throws NullPointerException{
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
