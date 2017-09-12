package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import moe.pinkd.netman.R;
import moe.pinkd.netman.ui.adapter.PackageInfoAdapter;
import moe.pinkd.netman.util.SharedPreferenceUtil;
import moe.pinkd.netman.util.ShellUtil;
import moe.pinkd.netman.util.StatusUpdater;
import moe.pinkd.netman.util.UISyncUtil;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private PackageInfoAdapter packageInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifyRestore();
        initView();
    }

    private void notifyRestore() {
        if (SharedPreferenceUtil.needRestore(this)) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, getText(R.string.restore_process_title), getText(R.string.waiting));
            UISyncUtil.runInBackground(new Runnable() {
                @Override
                public void run() {
                    ShellUtil.restoreRules(MainActivity.this);
                }
            }, new UISyncUtil.Callback(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    SharedPreferenceUtil.restored(MainActivity.this);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageInfoAdapter = new PackageInfoAdapter();
        packageInfoAdapter.setOnItemClickListener(new PackageInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("index", position));
            }
        });
        recyclerView.setAdapter(packageInfoAdapter);
        packageInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.action_show_system_apps).setChecked(packageInfoAdapter.isShowSystemApps());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_system_apps:
                item.setChecked(!item.isChecked());
                Log.d(TAG, "onMenuItemSelected: action_show_system_apps-->" + item.isChecked());
                packageInfoAdapter.setShowSystemApps(item.isChecked());
                break;
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
        return true;
    }
}
