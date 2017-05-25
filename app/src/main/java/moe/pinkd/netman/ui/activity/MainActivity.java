package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import moe.pinkd.netman.R;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.ui.adapter.PackageInfoAdapter;
import moe.pinkd.netman.util.PackageUtil;
import moe.pinkd.netman.util.SharedPreferenceUtil;
import moe.pinkd.netman.util.ShellUtil;

public class MainActivity extends Activity {

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
//            ProgressDialog progressDialog = ProgressDialog.show(this, getText(R.string.restore_process_title), null, true, false);
            ShellUtil.restoreRules(this);
            SharedPreferenceUtil.restored(this);
//            progressDialog.dismiss();
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageInfoAdapter = new PackageInfoAdapter(PackageUtil.getPackageInfo(), Config.NONE_MASK);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_system_apps:
                break;
            case R.id.action_modified_first:
                break;
        }
        item.setChecked(!item.isChecked());
        return super.onMenuItemSelected(featureId, item);
    }
}
