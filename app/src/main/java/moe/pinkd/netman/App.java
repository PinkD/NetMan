package moe.pinkd.netman;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import moe.pinkd.netman.util.SharedPreferenceUtil;
import moe.pinkd.netman.util.ShellUtil;

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
        if (SharedPreferenceUtil.loadCellularInterfaceName(this)) {//TODO move to detail activity
            ShellUtil.initCellularInterfaces();
        }
        ShellUtil.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext() {
        return context;
    }
}
