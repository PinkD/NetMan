package moe.pinkd.netman.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import moe.pinkd.netman.App;

/**
 * Created by PinkD on 2017/5/12.
 * PackageUtil
 */

public class PackageUtil {
    private static final String TAG = "PackageUtil";
    private static PackageManager packageManager;

    public static List<PackageInfo> getPackageInfo() {
        if (packageManager == null) {
            packageManager = App.getContext().getPackageManager();
        }
        return packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
    }

    public static List<ApplicationInfo> getApplicationInfo() {
        List<ApplicationInfo> applicationInfos = new ArrayList<>();
        for (PackageInfo packageInfo : getPackageInfo()) {
            applicationInfos.add(packageInfo.applicationInfo);
        }
        return applicationInfos;
    }

    public static Drawable loadIcon(ApplicationInfo applicationInfo) {
        return packageManager == null ? null : applicationInfo.loadIcon(packageManager);
    }

    public static Drawable loadIcon(PackageInfo packageInfo) {
        return packageManager == null ? null : packageInfo.applicationInfo.loadIcon(packageManager);
    }

    public static String loadLabel(ApplicationInfo applicationInfo) {
        return packageManager == null ? null : applicationInfo.loadLabel(packageManager).toString();
    }

    public static String loadLabel(PackageInfo packageInfo) {
        return packageManager == null ? null : packageInfo.applicationInfo.loadLabel(packageManager).toString();
    }
}
