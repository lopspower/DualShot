package com.mikhaellopez.dualshot.controller;

import android.app.Application;

import com.mikhaellopez.dualshot.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mikhael LOPEZ on 05/01/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/century_gothic.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
