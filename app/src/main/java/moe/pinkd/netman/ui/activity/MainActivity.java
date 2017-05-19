package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import moe.pinkd.netman.R;
import moe.pinkd.netman.ui.adapter.PackageInfoAdapter;
import moe.pinkd.netman.util.PackageUtil;

public class MainActivity extends Activity {

    private RecyclerView recyclerView;
    private PackageInfoAdapter packageInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageInfoAdapter = new PackageInfoAdapter(PackageUtil.getPackageInfo());
        packageInfoAdapter.setOnItemClickListener(new PackageInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("packageInfo", packageInfoAdapter.getItemAt(position)));
            }
        });
        recyclerView.setAdapter(packageInfoAdapter);
        packageInfoAdapter.notifyDataSetChanged();
    }

}
