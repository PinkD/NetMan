package moe.pinkd.netman.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.view.Window;
import android.view.WindowManager;

import moe.pinkd.netman.R;

public class NoRootActivity extends Activity implements DialogInterface.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.none);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.none));
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_root)
                .setPositiveButton(R.string.exit, this)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                Process.killProcess(Process.myPid());
                break;
        }
    }

}
