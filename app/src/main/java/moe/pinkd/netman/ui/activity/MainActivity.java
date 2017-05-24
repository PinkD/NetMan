package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import moe.pinkd.netman.R;
import moe.pinkd.netman.bean.AppStatus;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.ui.adapter.PackageInfoAdapter;
import moe.pinkd.netman.util.DatabaseUtil;
import moe.pinkd.netman.util.PackageUtil;

public class MainActivity extends Activity {

    private RecyclerView recyclerView;
    private PackageInfoAdapter packageInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        readFromDatabase();
    }

    private void readFromDatabase() {
        Map<Integer, Integer> record = DatabaseUtil.readRecord();
        List<AppStatus> appStatuses = packageInfoAdapter.getItems();
        for (AppStatus appStatus : appStatuses) {
            Integer tmp = record.get(appStatus.getPackageInfo().applicationInfo.uid);
            appStatus.setStatus(tmp == null ? 0 : tmp);
        }
        Collections.sort(appStatuses);
        packageInfoAdapter.notifyDataSetChanged();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageInfoAdapter = new PackageInfoAdapter(PackageUtil.getPackageInfo(), Config.NONE_MASK);
        packageInfoAdapter.setOnItemClickListener(new PackageInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("appInfo", packageInfoAdapter.getItemAt(position)));
            }
        });
        recyclerView.setAdapter(packageInfoAdapter);
        packageInfoAdapter.notifyDataSetChanged();
    }

}
