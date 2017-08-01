package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import moe.pinkd.netman.R;

public class AboutActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {

    private Dialog dialog;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.pay_pinkd_now).setOnClickListener(this);
        findViewById(R.id.about_developer).setOnClickListener(this);
        findViewById(R.id.about_app).setOnClickListener(this);
        findViewById(R.id.about_version_code).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_pinkd_now:
                if (dialog == null) {
                    dialog = new Dialog(this);
                    dialog.setTitle(R.string.donation);
                    dialog.setContentView(R.layout.pay_pinkd_now);
                    dialog.findViewById(R.id.address_btc).setOnClickListener(this);
                    dialog.findViewById(R.id.address_ali_pay).setOnClickListener(this);
                    clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                }
                dialog.show();
                break;
            case R.id.about_developer:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PinkD")));
                break;
            case R.id.about_app:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PinkD/NetMan")));
                break;
            case R.id.address_btc:
                clipboardManager.setPrimaryClip(ClipData.newPlainText(getText(R.string.btc), getText(R.string.btc_address)));
                Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
                break;
            case R.id.address_ali_pay:
                clipboardManager.setPrimaryClip(ClipData.newPlainText(getText(R.string.ali_pay), getText(R.string.ali_pay_address)));
                Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.about_version_code:
                Toast.makeText(AboutActivity.this, R.string.pay_pinkd_now, Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
