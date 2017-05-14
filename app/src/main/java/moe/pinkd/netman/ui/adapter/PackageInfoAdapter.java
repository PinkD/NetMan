package moe.pinkd.netman.ui.adapter;

import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moe.pinkd.netman.R;
import moe.pinkd.netman.util.PackageUtil;

/**
 * Created by PinkD on 2017/5/12.
 * Application Information Adapter
 */

public class PackageInfoAdapter extends RecyclerView.Adapter<PackageInfoAdapter.AppInfoViewHolder> {
    private static final String TAG = "PackageInfoAdapter";

    private List<PackageInfo> packageInfos;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public PackageInfoAdapter(List<PackageInfo> packageInfos) {
        this.packageInfos = packageInfos;
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
        Log.d(TAG, "bindContent--->" + packageInfos.get(position).packageName + ": " + packageInfos.get(position).applicationInfo.uid);
        holder.label.setText(PackageUtil.loadLabel(packageInfos.get(position)));
        holder.icon.setImageDrawable(PackageUtil.loadIcon(packageInfos.get(position)));
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

    public List<PackageInfo> getItems() {
        return packageInfos;
    }

    public PackageInfo getItemAt(int position) {
        return packageInfos.get(position);
    }


    @Override
    public int getItemCount() {
        return packageInfos.size();
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
