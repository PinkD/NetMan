package moe.pinkd.netman.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import moe.pinkd.netman.util.ShellUtil;


public class RestoreIptablesRulesService extends Service {
    private static final String TAG = "RestoreIptablesRules";
    public static final String ACTION_RESTORE_IPTABLES_RULES = "moe.pinkd.netman.component.service.action.RESTORE_IPTABLES_RULES";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " + intent);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RESTORE_IPTABLES_RULES.equals(action)) {
                Log.d(TAG, "ACTION_RESTORE_IPTABLES_RULES");
                ShellUtil.restoreRules(this);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
