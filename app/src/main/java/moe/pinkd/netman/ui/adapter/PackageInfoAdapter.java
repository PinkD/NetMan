package moe.pinkd.netman.ui.adapter;

import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moe.pinkd.netman.R;
import moe.pinkd.netman.bean.AppStatus;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.util.PackageUtil;

/**
 * Created by PinkD on 2017/5/12.
 * Application Information Adapter
 */

public class PackageInfoAdapter extends RecyclerView.Adapter<PackageInfoAdapter.AppInfoViewHolder> {
    private static final String TAG = "PackageInfoAdapter";

    private List<AppStatus> appStatuses;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;


    public PackageInfoAdapter(List<PackageInfo> packageInfos, int defaultMask) {
        List<AppStatus> appStatuses = new ArrayList<>();
        for (PackageInfo packageInfo : packageInfos) {
            appStatuses.add(new AppStatus(packageInfo, defaultMask));
        }
        this.appStatuses = appStatuses;
    }


    public PackageInfoAdapter(List<AppStatus> appStatuses) {
        this.appStatuses = appStatuses;
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
        Log.d(TAG, "bindContent--->" + appStatuses.get(position).getPackageInfo().packageName + ": " + appStatuses.get(position).getPackageInfo().applicationInfo.uid);
        holder.label.setText(PackageUtil.loadLabel(appStatuses.get(position).getPackageInfo()));
        if (appStatuses.get(position).getStatus() != Config.NONE_MASK) {
            holder.label.setTextColor(holder.label.getContext().getResources().getColor(R.color.black));
        }
        holder.icon.setImageDrawable(PackageUtil.loadIcon(appStatuses.get(position).getPackageInfo()));
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
        return appStatuses;
    }

    public AppStatus getItemAt(int position) {
        return appStatuses.get(position);
    }


    @Override
    public int getItemCount() {
        return appStatuses.size();
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

}
