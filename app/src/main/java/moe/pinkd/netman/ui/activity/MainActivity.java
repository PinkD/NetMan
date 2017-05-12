package moe.pinkd.netman.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import moe.pinkd.netman.R;
import moe.pinkd.netman.ui.adapter.AppInfoAdapter;
import moe.pinkd.netman.util.PackageUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;

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
        appInfoAdapter = new AppInfoAdapter(PackageUtil.getApplicationInfo());
        appInfoAdapter.setOnItemClickListener(new AppInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, position + appInfoAdapter.getItemAt(position).packageName, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(appInfoAdapter);
        appInfoAdapter.notifyDataSetChanged();
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
