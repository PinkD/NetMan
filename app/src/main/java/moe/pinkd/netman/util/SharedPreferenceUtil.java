package moe.pinkd.netman.util;

import android.content.Context;

import moe.pinkd.netman.config.Config;

/**
 * Created by PinkD on 2017/5/18.
 * SharedPreferenceUtil
 */

public class SharedPreferenceUtil {
    public static boolean loadCellularInterfaceName(Context context) {
        Config.CELLULAR_INTERFACE = context.getSharedPreferences("config", Context.MODE_PRIVATE).getString("cellular", null);
        return Config.CELLULAR_INTERFACE != null;
    }

    public static void saveCellularInterfaceName(Context context) {
        context.getSharedPreferences("config", Context.MODE_PRIVATE).edit().putString("cellular", Config.CELLULAR_INTERFACE).apply();
    }

    public static boolean needRestore(Context context) {
        return !context.getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("restore", false);
    }

    public static void setNeedRestore(Context context) {
        context.getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("restore", false).apply();
    }

    public static void restored(Context context) {
        context.getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("restore", true).apply();
    }

    public static void saveShowSystemApps(Context context, boolean save) {
        context.getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("showSystemApps", save).apply();
    }

    public static boolean getShowSystemApps(Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("showSystemApps", false);
    }
}
