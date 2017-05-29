package moe.pinkd.netman.bean;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;

import moe.pinkd.netman.config.Config;

/**
 * Created by PinkD on 2017/5/24.
 * AppStatus
 */

public class AppStatus implements Comparable<AppStatus> {
    private static final String TAG = "AppStatus";
    private PackageInfo packageInfo;
    private int status;

    public AppStatus(PackageInfo packageInfo, int status) {
        this.packageInfo = packageInfo;
        this.status = status;
    }
    public AppStatus(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
        this.status = Config.NONE_MASK;
    }

    protected AppStatus(Parcel in) {
        packageInfo = in.readParcelable(PackageInfo.class.getClassLoader());
        status = in.readInt();
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppStatus) {
            AppStatus tmp = (AppStatus) obj;
            boolean equal = (packageInfo.applicationInfo.uid == tmp.getPackageInfo().applicationInfo.uid) && (status == tmp.getStatus());
            if (equal) {
                Log.d(TAG, "equal");
            } else {
                Log.d(TAG, "not equal");
            }
            return equal;
        } else {
            Log.d(TAG, "not equal");
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull AppStatus appStatus) {
        return appStatus.getStatus() - status;
    }
}
