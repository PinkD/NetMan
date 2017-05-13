package moe.pinkd.netman.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import moe.pinkd.netman.R;
import moe.pinkd.netman.ui.adapter.PackageInfoAdapter;
import moe.pinkd.netman.util.PackageUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PackageInfoAdapter packageInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        test();
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


/*    private void test() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : ShellUtil.SURun(new String[]{"whoami", "ls -lah"})) {
            stringBuilder.append(s);
        }
        recyclerView.setText(stringBuilder.toString());
        recyclerView.setText(ShellUtil.shellRun("netstat -an"));
    }*/
}
