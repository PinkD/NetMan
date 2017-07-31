package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import moe.pinkd.netman.R;

public class AboutActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.pay_pinkd_now).setOnClickListener(this);
        findViewById(R.id.about_developer).setOnClickListener(this);
        findViewById(R.id.about_app).setOnClickListener(this);
        findViewById(R.id.open_source_license).setOnClickListener(this);
        findViewById(R.id.about_version_code).setOnLongClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_pinkd_now:
                Toast.makeText(AboutActivity.this, R.string.pay_pinkd_now, Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_developer:
                Toast.makeText(AboutActivity.this, R.string.about_developer, Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_app:
                Toast.makeText(AboutActivity.this, R.string.about_this_app, Toast.LENGTH_SHORT).show();
                break;

            case R.id.open_source_license:
                Toast.makeText(AboutActivity.this, R.string.about_license, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.about_version_code:
                Toast.makeText(AboutActivity.this, R.string.version_code, Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
