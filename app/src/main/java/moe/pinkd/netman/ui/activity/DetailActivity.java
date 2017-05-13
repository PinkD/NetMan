package moe.pinkd.netman.ui.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import moe.pinkd.netman.R;
import moe.pinkd.netman.util.PackageUtil;

public class DetailActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        PackageInfo packageInfo = getIntent().getParcelableExtra("packageInfo");
        ImageView icon = (ImageView) findViewById(R.id.detail_app_icon);
        TextView name = (TextView) findViewById(R.id.detail_app_name);
        TextView packageName = (TextView) findViewById(R.id.detail_app_package_name);
        TextView versionCode = (TextView) findViewById(R.id.detail_app_version_code);
        icon.setImageDrawable(PackageUtil.loadIcon(packageInfo));
        name.setText(PackageUtil.loadLabel(packageInfo));
        packageName.setText(packageInfo.packageName);
        versionCode.setText(String.valueOf(packageInfo.versionName));
        Switch cellular = (Switch) findViewById(R.id.detail_permission_cellular_switch);
        Switch wifi = (Switch) findViewById(R.id.detail_permission_wifi_switch);
        Switch vpn = (Switch) findViewById(R.id.detail_permission_vpn_switch);
        cellular.setOnCheckedChangeListener(this);
        wifi.setOnCheckedChangeListener(this);
        vpn.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.detail_permission_cellular_switch:
                Toast.makeText(this, R.string.cellular, Toast.LENGTH_SHORT).show();
                break;
            case R.id.detail_permission_wifi_switch:
                Toast.makeText(this, R.string.wifi, Toast.LENGTH_SHORT).show();
                break;
            case R.id.detail_permission_vpn_switch:
                Toast.makeText(this, R.string.vpn, Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
