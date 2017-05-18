package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import moe.pinkd.netman.R;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.util.PackageUtil;
import moe.pinkd.netman.util.ShellUtil;

public class DetailActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private Switch all;
    private Switch cellular;
    private Switch wifi;
    private Switch vpn;
    private PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        packageInfo = getIntent().getParcelableExtra("packageInfo");
        ImageView icon = (ImageView) findViewById(R.id.detail_app_icon);
        TextView name = (TextView) findViewById(R.id.detail_app_name);
        TextView packageName = (TextView) findViewById(R.id.detail_app_package_name);
        TextView versionCode = (TextView) findViewById(R.id.detail_app_version_code);
        icon.setImageDrawable(PackageUtil.loadIcon(packageInfo));
        name.setText(PackageUtil.loadLabel(packageInfo));
        packageName.setText(packageInfo.packageName);
        versionCode.setText(String.valueOf(packageInfo.versionName));
        all = (Switch) findViewById(R.id.detail_permission_all_switch);
        cellular = (Switch) findViewById(R.id.detail_permission_cellular_switch);
        wifi = (Switch) findViewById(R.id.detail_permission_wifi_switch);
        vpn = (Switch) findViewById(R.id.detail_permission_vpn_switch);
        all.setOnCheckedChangeListener(this);
        cellular.setOnCheckedChangeListener(this);
        wifi.setOnCheckedChangeListener(this);
        vpn.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int status = 0;
        if (buttonView.getId() == all.getId()) {
            if (all.isChecked()) {
                enableAll();
                ShellUtil.banApp(packageInfo.applicationInfo.uid, Config.ALL_MASK);
                return;
            } else {
                disableAll();
            }
        }
        if (cellular.isChecked()) {
            status |= Config.CELLULAR_MASK;
        }
        if (wifi.isChecked()) {
            status |= Config.WIFI_MASK;
        }
        if (vpn.isChecked()) {
            status |= Config.VPN_MASK;
        }
        ShellUtil.banApp(packageInfo.applicationInfo.uid, status);
    }

    private void enableAll() {
        cellular.setEnabled(false);
        wifi.setEnabled(false);
        vpn.setEnabled(false);
    }

    private void disableAll() {
        cellular.setEnabled(true);
        wifi.setEnabled(true);
        vpn.setEnabled(true);
    }
}
