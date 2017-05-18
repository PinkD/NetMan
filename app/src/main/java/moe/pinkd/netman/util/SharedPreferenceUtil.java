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
}
