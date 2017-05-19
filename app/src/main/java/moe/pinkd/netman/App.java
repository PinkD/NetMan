package moe.pinkd.netman;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import moe.pinkd.netman.util.DatabaseUtil;
import moe.pinkd.netman.util.SharedPreferenceUtil;
import moe.pinkd.netman.util.ShellUtil;

/**
 * Created by PinkD on 2017/5/11.
 * Default Application
 */

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ShellUtil.init(this);
        DatabaseUtil.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext() {
        return context;
    }
}
