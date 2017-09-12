package moe.pinkd.netman.ui.adapter;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import moe.pinkd.netman.App;
import moe.pinkd.netman.R;
import moe.pinkd.netman.bean.AppStatus;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.util.DatabaseUtil;
import moe.pinkd.netman.util.PackageUtil;
import moe.pinkd.netman.util.SharedPreferenceUtil;
import moe.pinkd.netman.util.StatusUpdater;

/**
 * Created by PinkD on 2017/5/12.
 * Application Information Adapter
 */

public class PackageInfoAdapter extends RecyclerView.Adapter<PackageInfoAdapter.AppInfoViewHolder> implements Observer {
    private static final String TAG = "PackageInfoAdapter";

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private boolean showSystemApps;

    public PackageInfoAdapter() {
        showSystemApps = SharedPreferenceUtil.getShowSystemApps(App.getContext());
        loadList();
    }

    private void loadList() {
        List<AppStatus> appStatuses = new ArrayList<>();
        List<PackageInfo> packageInfos = PackageUtil.getPackageInfo();
        for (PackageInfo packageInfo : packageInfos) {
            if (packageInfo.applicationInfo.uid > 9999 && isSystemApp(packageInfo)) {
                appStatuses.add(new AppStatus(packageInfo));
            }
        }
        init(appStatuses);
    }

    private boolean isSystemApp(PackageInfo packageInfo) {
        return showSystemApps || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
    }


    private void init(List<AppStatus> appStatuses) {
        StatusUpdater.GLOBAL_APP_STATUS.addAll(appStatuses);
        readFromDatabase();
        StatusUpdater.addStatusUpdate(this);
    }

    private void readFromDatabase() {
        Map<Integer, Integer> record = DatabaseUtil.readRecord();
        for (AppStatus appStatus : StatusUpdater.GLOBAL_APP_STATUS) {
            Integer tmp = record.get(appStatus.getPackageInfo().applicationInfo.uid);
            appStatus.setStatus(tmp == null ? 0 : tmp);
            Log.d(TAG, "readFromDatabase: " + appStatus.getPackageInfo().applicationInfo.packageName + " ---> " + appStatus.getStatus());
        }
        update();
    }

    private void update() {
        Log.d(TAG, "update: ");
        Collections.sort(StatusUpdater.GLOBAL_APP_STATUS);
        notifyDataSetChanged();
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info, parent, false));
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        bindListener(holder);
        bindContent(holder, position);
    }

    private void bindContent(AppInfoViewHolder holder, int position) {
        holder.label.setText(PackageUtil.loadLabel(StatusUpdater.GLOBAL_APP_STATUS.get(position).getPackageInfo()));
        if (StatusUpdater.GLOBAL_APP_STATUS.get(position).getStatus() != Config.NONE_MASK) {
            holder.label.setTextColor(holder.label.getContext().getResources().getColor(R.color.colorAccent));
        } else {
            holder.label.setTextColor(holder.label.getContext().getResources().getColor(R.color.black));
        }
        holder.icon.setImageDrawable(PackageUtil.loadIcon(StatusUpdater.GLOBAL_APP_STATUS.get(position).getPackageInfo()));
    }

    private void bindListener(final AppInfoViewHolder holder) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick(holder.getAdapterPosition());
                }
            });
        }
    }

    public List<AppStatus> getItems() {
        return StatusUpdater.GLOBAL_APP_STATUS;
    }

    public AppStatus getItemAt(int position) {
        return StatusUpdater.GLOBAL_APP_STATUS.get(position);
    }


    @Override
    public int getItemCount() {
        return StatusUpdater.GLOBAL_APP_STATUS.size();
    }

    @Override
    public void update(Observable o, Object arg) {
        update();
    }

    class AppInfoViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView label;

        public AppInfoViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_app_information_icon);
            label = (TextView) itemView.findViewById(R.id.item_app_information_name);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public boolean isShowSystemApps() {
        return showSystemApps;
    }

    public void setShowSystemApps(boolean showSystemApps) {
        if (this.showSystemApps != showSystemApps) {
            this.showSystemApps = showSystemApps;
            StatusUpdater.GLOBAL_APP_STATUS.clear();
            loadList();
            SharedPreferenceUtil.saveShowSystemApps(App.getContext(), showSystemApps);
            Log.d(TAG, "setShowSystemApps: update");
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

}
