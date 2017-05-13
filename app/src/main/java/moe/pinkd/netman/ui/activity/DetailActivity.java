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
import moe.pinkd.netman.util.ShellUtil;

public class DetailActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch all;
    private Switch cellular;
    private Switch wifi;
    private Switch vpn;

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
        switch (buttonView.getId()) {
            case R.id.detail_permission_all_switch:
                Toast.makeText(this, R.string.all, Toast.LENGTH_SHORT).show();
                if (buttonView.isSelected()) {
                    enableAll();
                } else {
                    disableAll();
                }
                break;
            case R.id.detail_permission_cellular_switch:
                Toast.makeText(this, R.string.cellular, Toast.LENGTH_SHORT).show();
                if (buttonView.isSelected()) {
                    enableAll();
                } else {
                    disableAll();
                }
                buttonView.setChecked(buttonView.isChecked());
                break;
            case R.id.detail_permission_wifi_switch:
                Toast.makeText(this, R.string.wifi, Toast.LENGTH_SHORT).show();
                if (buttonView.isSelected()) {
                    enableAll();
                } else {
                    disableAll();
                }
                buttonView.setChecked(buttonView.isChecked());
                break;
            case R.id.detail_permission_vpn_switch:
                Toast.makeText(this, R.string.vpn, Toast.LENGTH_SHORT).show();
                if (buttonView.isSelected()) {
                    //TODO enable all
                    enableAll();
                } else {
                    //TODO disable all
                    disableAll();
                }
                buttonView.setChecked(buttonView.isChecked());
                break;

        }
    }

    private void enableAll() {
        cellular.setChecked(true);
        wifi.setChecked(true);
        vpn.setChecked(true);
        ShellUtil.SURun(new String[]{"iptables -t filter -m owner --owner"});
    }

    private void disableAll() {
        cellular.setChecked(false);
        wifi.setChecked(false);
        vpn.setChecked(false);
    }
}
