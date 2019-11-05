package com.wan.natives;

import android.app.Application;

/**
 * Created by wan on 2016/6/20.
 */
public class BaseApplication extends Application {
    private static BaseApplication application;


    public static BaseApplication getApplication() {
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

}
