package com.googgler.wallpaper;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

        OneSignal.startInit(this).init();
        Fresco.initialize(this);
    }
}
