package moe.pinkd.netman;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by PinkD on 2017/5/11.
 * Default Application
 */

public class App extends Application {
    private static final String TAG = "App";

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext() {
        return context;
    }
}
