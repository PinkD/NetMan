package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import moe.pinkd.netman.R;
import moe.pinkd.netman.bean.AppStatus;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.util.PackageUtil;
import moe.pinkd.netman.util.ShellUtil;
import moe.pinkd.netman.util.StatusUpdater;

public class DetailActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "DetailActivity";

    private Switch all;
    private Switch cellular;
    private Switch wifi;
    private Switch vpn;
    private int index;
    private AppStatus appStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        index = getIntent().getIntExtra("index", -1);
        if (index < 0) {
            Toast.makeText(this, R.string.app_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            appStatus = StatusUpdater.GLOBAL_APP_STATUS.get(index);
        }
        ImageView icon = (ImageView) findViewById(R.id.detail_app_icon);
        TextView name = (TextView) findViewById(R.id.detail_app_name);
        TextView packageName = (TextView) findViewById(R.id.detail_app_package_name);
        TextView versionCode = (TextView) findViewById(R.id.detail_app_version_code);
        icon.setImageDrawable(PackageUtil.loadIcon(appStatus.getPackageInfo()));
        name.setText(PackageUtil.loadLabel(appStatus.getPackageInfo()));
        packageName.setText(appStatus.getPackageInfo().packageName);
        versionCode.setText(String.valueOf(appStatus.getPackageInfo().versionName));
        all = (Switch) findViewById(R.id.detail_permission_all_switch);
        cellular = (Switch) findViewById(R.id.detail_permission_cellular_switch);
        wifi = (Switch) findViewById(R.id.detail_permission_wifi_switch);
        vpn = (Switch) findViewById(R.id.detail_permission_vpn_switch);
        initCheckStatus();
        all.setOnCheckedChangeListener(this);
        cellular.setOnCheckedChangeListener(this);
        wifi.setOnCheckedChangeListener(this);
        vpn.setOnCheckedChangeListener(this);
    }

    private void initCheckStatus() {
        int status = appStatus.getStatus();
        if ((status & Config.ALL_MASK) == Config.ALL_MASK) {
            Log.d(TAG, "banApp: ALL_MASK");
            enableAll();
            all.setChecked(true);
            return;
        }
        if ((status & Config.CELLULAR_MASK) == Config.CELLULAR_MASK) {
            Log.d(TAG, "banApp: CELLULAR_MASK");
            cellular.setChecked(true);
        }
        if ((status & Config.WIFI_MASK) == Config.WIFI_MASK) {
            Log.d(TAG, "banApp: WIFI_MASK");
            wifi.setChecked(true);
        }
        if ((status & Config.VPN_MASK) == Config.VPN_MASK) {
            Log.d(TAG, "banApp: VPN_MASK");
            vpn.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int status = 0;
        if (buttonView.getId() == all.getId()) {
            if (all.isChecked()) {
                enableAll();
                ShellUtil.banApp(this, appStatus.getPackageInfo().applicationInfo.uid, Config.ALL_MASK);
                appStatus.setStatus(Config.ALL_MASK);
                StatusUpdater.notifyStatusUpdate();
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
        appStatus.setStatus(status);
        ShellUtil.banApp(this, appStatus.getPackageInfo().applicationInfo.uid, status);
        StatusUpdater.notifyStatusUpdate();
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
