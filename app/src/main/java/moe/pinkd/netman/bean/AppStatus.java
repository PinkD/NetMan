package moe.pinkd.netman.bean;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by PinkD on 2017/5/24.
 * AppStatus
 */

public class AppStatus implements Parcelable, Comparable<AppStatus> {
    private PackageInfo packageInfo;
    private int status;

    public AppStatus(PackageInfo packageInfo, int status) {
        this.packageInfo = packageInfo;
        this.status = status;
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
    public int compareTo(@NonNull AppStatus appStatus) {
        return status - appStatus.getStatus();
    }

    public static final Creator<AppStatus> CREATOR = new Creator<AppStatus>() {
        @Override
        public AppStatus createFromParcel(Parcel in) {
            return new AppStatus(in);
        }

        @Override
        public AppStatus[] newArray(int size) {
            return new AppStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(packageInfo, flags);
        dest.writeInt(status);
    }
}
