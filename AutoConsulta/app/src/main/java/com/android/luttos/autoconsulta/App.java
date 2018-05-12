package com.android.luttos.autoconsulta;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by pesso on 12/05/2018.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
