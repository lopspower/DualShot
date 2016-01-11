package com.mikhaellopez.dualshot.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Copyright (C) 2016 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * TransitionUtils to start an Activity with transition
 */
public class TransitionUtils {

    public static void startActivity(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(activity, intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            startActivity(activity, intent, null);
        }
    }

    public static void startActivity(Activity activity, Intent intent, @Nullable Bundle options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent, options);
        } else {
            activity.startActivity(intent);
        }
    }

}
